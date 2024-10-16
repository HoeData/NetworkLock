package com.ruoyi.web.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.web.domain.LockAuthorizationLog;
import com.ruoyi.web.domain.RelPdaUserPort;
import com.ruoyi.web.domain.vo.pda.RelPdaUserPortParamVO;
import com.ruoyi.web.domain.vo.pda.RelPdaUserPortViewVO;
import com.ruoyi.web.domain.vo.port.LockInfoVO;
import com.ruoyi.web.mapper.LockPdaInfoMapper;
import com.ruoyi.web.mapper.LockPdaUserMapper;
import com.ruoyi.web.mapper.RelPdaUserPortMapper;
import com.ruoyi.web.service.ILockAuthorizationLogService;
import com.ruoyi.web.service.IRelPdaUserPortService;
import com.ruoyi.web.utils.LockUtil;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RelPdaUserPortServiceImpl extends
    ServiceImpl<RelPdaUserPortMapper, RelPdaUserPort> implements IRelPdaUserPortService {

    private final RelPdaUserPortMapper pdaUserPortMapper;
    private final LockPdaUserMapper pdaUserMapper;
    private final ILockAuthorizationLogService lockAuthorizationLogService;
    private final LockPdaInfoMapper lockPdaInfoMapper;

    @Override
    public List<RelPdaUserPortViewVO> getAllList(RelPdaUserPortParamVO vo) {
        List<RelPdaUserPortViewVO> list = pdaUserPortMapper.selectAllList(vo);
        return list;
    }

    @Override
    public List<RelPdaUserPortViewVO> getAuthorizationList(RelPdaUserPortParamVO vo) {
        List<RelPdaUserPortViewVO> list = pdaUserPortMapper.selectAuthorizationList(vo);
        return list;
    }

    @Override
    @Transactional
    public int saveAuthorization(List<RelPdaUserPort> list) {
        List<RelPdaUserPort> addList = new ArrayList<>();
        LockAuthorizationLog lockAuthorizationLog = new LockAuthorizationLog();
        lockAuthorizationLog.setPdaUserId(list.get(0).getPdaUserId());
        lockAuthorizationLog.setCreateTime(new Date());
        lockAuthorizationLog.setCreateBy(SecurityUtils.getLoginUser().getUserId().toString());
        lockAuthorizationLog.setSuccessFlag(0);
        lockAuthorizationLogService.save(lockAuthorizationLog);
        try {
            if (lockPdaInfoMapper.selectById(pdaUserMapper.selectById(list.get(0).getPdaUserId()).getPdaId())
                .getType() == 0) {
                RelPdaUserPortParamVO relPdaUserPortParamVO = new RelPdaUserPortParamVO();
                relPdaUserPortParamVO.setPdaUserId(list.get(0).getPdaUserId());
                List<RelPdaUserPortViewVO> authorizationList = getAuthorizationList(
                    relPdaUserPortParamVO);
                Set<String> set = new HashSet<>();
                authorizationList.forEach(item -> {
                    set.add(item.getPortInfoId().toString());
                });
                list.forEach(item -> {
                    if (!set.contains(item.getPortInfoId())) {
                        item.setCreateTime(new Date());
                        addList.add(item);
                    }
                });
                return saveBatch(addList) ? 1 : 0;
            } else {
                list.forEach(item -> item.setCreateTime(new Date()));
                return saveBatch(list) ? 1 : 0;
            }
        } catch (Throwable t) {
            lockAuthorizationLog.setErrorMsg(
                StringUtils.isNotBlank(t.getMessage()) ? t.getMessage().substring(0, 200)
                    : "未知错误");
            lockAuthorizationLog.setSuccessFlag(1);
            lockAuthorizationLogService.updateById(lockAuthorizationLog);
            throw new ServiceException("授权失败");
        }
    }

    @Override
    public Map<String, Object> getHexMessageForAddLock(List<RelPdaUserPort> relPdaUserPortList) {
        long day = DateUtil.between(relPdaUserPortList.get(0).getValidityPeriod(), new Date(),
            DateUnit.DAY);
        if (day > 254) {
            throw new ServiceException("授权有效期过长");
        }
        setSerialNumber(relPdaUserPortList);
        List<LockInfoVO> lockInfoList = new ArrayList<>();
        List<String> hexMessageList = new ArrayList<>();
        for (RelPdaUserPort item : relPdaUserPortList) {
            if (lockInfoList.size() == 48) {
                hexMessageList.add(
                    LockUtil.bytesToHexWithSpaces(LockUtil.getByteForAddLock(lockInfoList)));
                hexMessageList = new ArrayList<>();
            }
            LockInfoVO lockInfo = new LockInfoVO();
            lockInfo.setLockNumber(
                (byte) Integer.parseInt(Integer.toHexString(item.getSerialNumber()), 16));
            lockInfo.setLockSerialNumber(
                item.getLockSerialNumber().getBytes(StandardCharsets.US_ASCII));
            lockInfo.setLockEffective((byte) Integer.parseInt(Integer.toHexString(4), 16));
            lockInfo.setLockTime(
                (byte) Integer.parseInt(Integer.toHexString(Integer.parseInt(String.valueOf(day))),
                    16));
            lockInfoList.add(lockInfo);
        }
        if (lockInfoList.size() > 0) {
            hexMessageList.add(
                LockUtil.bytesToHexWithSpaces(LockUtil.getByteForAddLock(lockInfoList)));
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("addData", relPdaUserPortList);
        resultMap.put("hexMessageList", hexMessageList);
        return resultMap;
    }

    @Override
    public String getHexMessageForDelLock(String[] ids) {
        LambdaQueryWrapper<RelPdaUserPort> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(RelPdaUserPort::getId, ids);
        List<RelPdaUserPort> list=list(queryWrapper);
        List<LockInfoVO> lockInfoList = new ArrayList<>();
        list.forEach(lockPortInfo -> {
            LockInfoVO lockInfo = new LockInfoVO();
            lockInfo.setLockNumber(
                (byte) Integer.parseInt(Integer.toHexString(lockPortInfo.getSerialNumber()), 16));
            lockInfoList.add(lockInfo);
        });
        return LockUtil.bytesToHexWithSpaces(LockUtil.getByteForDelLock(lockInfoList));
    }

    private void setSerialNumber(List<RelPdaUserPort> relPdaUserPortList) {
        List<RelPdaUserPort> list = pdaUserPortMapper.selectByPdaUserId(
            relPdaUserPortList.get(0).getPdaUserId());
        if (list.size() + relPdaUserPortList.size() > 255) {
            throw new ServiceException("电子钥匙最多授权255把锁");
        }
        Map<Integer, Boolean> existMap = list.stream()
            .collect(Collectors.toMap(RelPdaUserPort::getSerialNumber, vo -> true, (a, b) -> b));
        int endSize = 0;
        int size = relPdaUserPortList.size();
        int listIndex = 0;
        for (int i = 1; i <= 255; i++) {
            if (endSize == size) {
                break;
            }
            if (existMap.getOrDefault(i, false)) {
                continue;
            }
            relPdaUserPortList.get(listIndex).setSerialNumber(i);
            listIndex++;
            endSize++;
        }
    }

}
