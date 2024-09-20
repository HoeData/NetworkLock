package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.web.domain.LockAuthorizationLog;
import com.ruoyi.web.domain.LockPdaInfo;
import com.ruoyi.web.domain.LockPortInfo;
import com.ruoyi.web.domain.RelPdaUserPort;
import com.ruoyi.web.domain.vo.pda.RelPdaUserPortParamVO;
import com.ruoyi.web.domain.vo.pda.RelPdaUserPortViewVO;
import com.ruoyi.web.domain.vo.pda.RelPortViewVO;
import com.ruoyi.web.mapper.LockPdaInfoMapper;
import com.ruoyi.web.mapper.LockPdaUserMapper;
import com.ruoyi.web.mapper.RelPdaUserPortMapper;
import com.ruoyi.web.service.ILockAuthorizationLogService;
import com.ruoyi.web.service.ILockPortInfoService;
import com.ruoyi.web.service.IRelPdaUserPortService;
import com.ruoyi.web.utils.PdaDataSynchronizationUtil;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RelPdaUserPortServiceImpl extends
    ServiceImpl<RelPdaUserPortMapper, RelPdaUserPort> implements IRelPdaUserPortService {

    private final RelPdaUserPortMapper pdaUserPortMapper;
    private final ILockPortInfoService lockPortInfoService;
    private final LockPdaInfoMapper pdaInfoMapper;
    private final LockPdaUserMapper pdaUserMapper;
    private final ILockAuthorizationLogService lockAuthorizationLogService;

    @Override
    public List<RelPdaUserPortViewVO> getAllList(RelPdaUserPortParamVO vo) {
        List<RelPdaUserPortViewVO> list = pdaUserPortMapper.selectAllList(vo);
        setPortViewList(list, lockPortInfoService.getAll());
        return list;
    }

    @Override
    public List<RelPdaUserPortViewVO> getAuthorizationList(RelPdaUserPortParamVO vo) {
        List<RelPdaUserPortViewVO> list = pdaUserPortMapper.selectAuthorizationList(vo);
        setPortViewList(list, pdaUserPortMapper.selectAuthorizationPortList(vo.getPdaUserId()));
        return list;
    }

    @Override
    @Transactional
    public int saveAuthorization(List<RelPdaUserPort> list) {
        LockAuthorizationLog lockAuthorizationLog = new LockAuthorizationLog();
        lockAuthorizationLog.setPdaUserId(list.get(0).getPdaUserId());
        lockAuthorizationLog.setCreateTime(new Date());
        lockAuthorizationLog.setCreateBy(SecurityUtils.getLoginUser().getUserId().toString());
        lockAuthorizationLog.setSuccessFlag(0);
        lockAuthorizationLogService.save(lockAuthorizationLog);
        try {
            LockPdaInfo lockPdaInfo = pdaInfoMapper.selectById(
                pdaUserMapper.selectById(list.get(0).getPdaUserId()).getPdaId());
            pdaUserPortMapper.deleteByPdaUserId(list.get(0).getPdaUserId());
            if (lockPdaInfo.getType() == 1) {
                return saveBatch(list) ? 1 : 0;
            }
            PdaDataSynchronizationUtil.startPdaAuthorization(list);
            return saveBatch(list) ? 1 : 0;
        } catch (Throwable t) {
            lockAuthorizationLog.setErrorMsg(
                StringUtils.isNotBlank(t.getMessage()) ? t.getMessage() : "未知错误");
            lockAuthorizationLog.setSuccessFlag(1);
            lockAuthorizationLogService.updateById(lockAuthorizationLog);
            throw new ServiceException("授权失败");
        }

    }

    private void setPortViewList(List<RelPdaUserPortViewVO> list, List<LockPortInfo> portInfoList) {
        Map<Integer, List<RelPortViewVO>> map = getLockPortInfoMap(portInfoList);
        list.forEach(item -> {
            if (map.containsKey(item.getEquipmentId())) {
                item.setPortViewList(map.get(item.getEquipmentId()));
            }
        });
    }

    private Map<Integer, List<RelPortViewVO>> getLockPortInfoMap(List<LockPortInfo> list) {
        Map<Integer, List<RelPortViewVO>> map = new HashMap<>();
        list.forEach(lockPortInfo -> {
            RelPortViewVO relPortViewVO = new RelPortViewVO();
            relPortViewVO.setPortInfoId(lockPortInfo.getId());
            relPortViewVO.setPortInfoIndex(lockPortInfo.getSerialNumber());
            relPortViewVO.setPortInfoUserCode(lockPortInfo.getUserCode());
            if (map.containsKey(lockPortInfo.getEquipmentId())) {
                map.get(lockPortInfo.getEquipmentId()).add(relPortViewVO);
            } else {
                List<RelPortViewVO> relPortViewVOList = new java.util.ArrayList<>();
                relPortViewVOList.add(relPortViewVO);
                map.put(lockPortInfo.getEquipmentId(), relPortViewVOList);
            }
        });
        return map;
    }
}
