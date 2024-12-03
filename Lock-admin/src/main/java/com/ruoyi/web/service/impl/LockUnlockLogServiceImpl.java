package com.ruoyi.web.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.web.annotation.CompanyScope;
import com.ruoyi.web.constants.CommonConst;
import com.ruoyi.web.domain.LockPdaUser;
import com.ruoyi.web.domain.LockPortInfo;
import com.ruoyi.web.domain.LockUnlockLog;
import com.ruoyi.web.domain.vo.pda.LockPdaUserPageParamVO;
import com.ruoyi.web.domain.vo.pda.LockUnlockViewVO;
import com.ruoyi.web.domain.vo.pda.UnlockPageParamVO;
import com.ruoyi.web.domain.vo.port.LockPortInfoListParamVO;
import com.ruoyi.web.domain.vo.port.LockUnlockHexMessageVO;
import com.ruoyi.web.mapper.LockPdaUserMapper;
import com.ruoyi.web.mapper.LockPortInfoMapper;
import com.ruoyi.web.mapper.LockUnlockLogMapper;
import com.ruoyi.web.service.ILockUnlockLogService;
import com.ruoyi.web.utils.LockUtil;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LockUnlockLogServiceImpl extends
    ServiceImpl<LockUnlockLogMapper, LockUnlockLog> implements ILockUnlockLogService {

    private final LockUnlockLogMapper unlockLogMapper;
    private final LockPdaUserMapper lockPdaUserMapper;
    private final LockPortInfoMapper lockPortInfoMapper;

    @Override
    @CompanyScope()
    public List<LockUnlockViewVO> selectUnlockList(UnlockPageParamVO pageVO) {
        return unlockLogMapper.selectUnlockList(pageVO);
    }


    @SneakyThrows
    @Async
    @Override
    public void syncKeyUnlockLog(LockUnlockHexMessageVO lockUnlockHexMessageVO) {
        Map<String, Integer> serialNumberIdMap = lockPortInfoMapper.selectAllList(
                new LockPortInfoListParamVO()).stream()
            .filter(portInfo -> StringUtils.isNotBlank(portInfo.getUserCode()))
            .collect(Collectors.toMap(LockPortInfo::getUserCode, LockPortInfo::getId, (a, b) -> b));
        LockPdaUser pdaUser = lockPdaUserMapper.selectPdaUserList(
                LockPdaUserPageParamVO.builder().pdaId(lockUnlockHexMessageVO.getPdaInfoId()).build())
            .get(CommonConst.ZERO);
        lockUnlockHexMessageVO.getHexMessageList().forEach(hexMessage -> {
            hexMessage = hexMessage.replace(" ", "");
            String dataHex = hexMessage.substring(10, hexMessage.length() - 2);
            List<String> list = Lists.newArrayList();
            for (int i = 0; i < dataHex.length(); i += 46) {
                String str = dataHex.substring(i, Math.min(i + 46, dataHex.length()));
                list.add(str);
            }
            int dataSize = Integer.parseInt(hexMessage.substring(8, 10), 16);
            if (dataSize > 0) {
                List<LockUnlockLog> unlockLogList = Lists.newArrayList();
                LockUnlockLog unlockLog;
                if (list.size() > CommonConst.ZERO) {
                    for (String str : list) {
                        String serialNumber = new String(
                            LockUtil.hexStringToByteArray(str.substring(0, 32), 2),
                            StandardCharsets.US_ASCII);
                        String unlockTime = str.substring(32, 44);
                        LocalDateTime unlockTimeDate = LocalDateTime.of(
                            2000 + Integer.parseInt(unlockTime.substring(0, 2), 16),
                            Integer.parseInt(unlockTime.substring(2, 4), 16),
                            Integer.parseInt(unlockTime.substring(4, 6), 16),
                            Integer.parseInt(unlockTime.substring(6, 8), 16),
                            Integer.parseInt(unlockTime.substring(8, 10), 16),
                            Integer.parseInt(unlockTime.substring(10, 12), 16));
                        unlockLog = new LockUnlockLog();
                        unlockLog.setId(UUID.fastUUID().toString());
                        unlockLog.setPdaUserId(pdaUser.getId());
                        unlockLog.setCreateTime(unlockTimeDate);
                        unlockLog.setLockSerialNumber(serialNumber);
                        unlockLog.setPortId(serialNumberIdMap.get(serialNumber));
                        unlockLog.setStatus(
                            StringUtils.equalsIgnoreCase(str.substring(44, 46), "7F") ? 1 : 2);
                        unlockLog.setErrorMsg(
                            unlockLog.getStatus() == 1 ? "开锁成功!" : "开锁失败!");
                        unlockLogList.add(unlockLog);
                    }
                    unlockLogList = filterExist(unlockLogList);
                    if (CollectionUtil.isNotEmpty(unlockLogList)) {
                        saveBatch(unlockLogList);
                    }
                }
            }
        });
    }

    private List<LockUnlockLog> filterExist(List<LockUnlockLog> unlockLogList) {
        if (CollectionUtil.isEmpty(unlockLogList)) {
            return Lists.newArrayList();
        }
        List<LockUnlockViewVO> list = unlockLogMapper.selectUnlockList(UnlockPageParamVO.builder()
            .queryStartDate(unlockLogList.get(unlockLogList.size() - 1).getCreateTime())
            .pdaUserId(unlockLogList.get(0).getPdaUserId()).build());
        Set<String> set = list.stream().map(viewVO -> viewVO.getCreateTime().toString())
            .collect(Collectors.toSet());
        return unlockLogList.stream().filter(log -> !set.contains(log.getCreateTime().toString()))
            .collect(Collectors.toList());
    }
}
