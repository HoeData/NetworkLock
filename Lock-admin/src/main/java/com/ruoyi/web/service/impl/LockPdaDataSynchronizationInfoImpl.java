package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.domain.LockPdaDataSynchronizationInfo;
import com.ruoyi.web.domain.LockPdaDataSynchronizationProcess;
import com.ruoyi.web.domain.LockPdaInfo;
import com.ruoyi.web.enums.PdaDataSynchronizationStatusType;
import com.ruoyi.web.enums.PdaDataSynchronizationType;
import com.ruoyi.web.mapper.LockPdaDataSynchronizationInfoMapper;
import com.ruoyi.web.service.ILockPdaDataSynchronizationInfoService;
import com.ruoyi.web.service.ILockPdaDataSynchronizationProcessService;
import com.ruoyi.web.service.ILockPdaInfoService;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LockPdaDataSynchronizationInfoImpl extends
    ServiceImpl<LockPdaDataSynchronizationInfoMapper, LockPdaDataSynchronizationInfo> implements
    ILockPdaDataSynchronizationInfoService {

    private final ILockPdaInfoService iLockPdaInfoService;
    private final ILockPdaDataSynchronizationProcessService processService;
    private final LockPdaDataSynchronizationInfoMapper infoMapper;

    @Override
    public LockPdaDataSynchronizationInfo saveAll(String deviceId,
        PdaDataSynchronizationType pdaDataSynchronizationType) {
        LockPdaInfo lockPdaInfo = iLockPdaInfoService.getByKey(deviceId);
        if (null != lockPdaInfo) {
            LockPdaDataSynchronizationInfo lockPdaDataSynchronizationInfo = new LockPdaDataSynchronizationInfo();
            lockPdaDataSynchronizationInfo.setType(pdaDataSynchronizationType.getValue());
            lockPdaDataSynchronizationInfo.setPdaId(lockPdaInfo.getId());
            lockPdaDataSynchronizationInfo.setCreateTime(new Date());
            lockPdaDataSynchronizationInfo.setStatus(
                PdaDataSynchronizationStatusType.START.getValue());
            save(lockPdaDataSynchronizationInfo);
            LockPdaDataSynchronizationProcess process = setLockPdaDataSynchronizationProcess(
                lockPdaDataSynchronizationInfo);
            processService.save(process);
            return lockPdaDataSynchronizationInfo;
        }
        return null;
    }

    @Override
    public void updateStatus(LockPdaDataSynchronizationInfo lockPdaDataSynchronizationInfo,
        Integer status) {
        lockPdaDataSynchronizationInfo.setStatus(status);
        lockPdaDataSynchronizationInfo.setUpdateTime(new Date());
        updateById(lockPdaDataSynchronizationInfo);
        LockPdaDataSynchronizationProcess process = setLockPdaDataSynchronizationProcess(
            lockPdaDataSynchronizationInfo);
        process.setStatus(status);
        processService.save(process);
    }

    @Override
    public LockPdaDataSynchronizationInfo getLastByDeviceId(String deviceId) {
        return infoMapper.getLastByDeviceId(deviceId);
    }

    private LockPdaDataSynchronizationProcess setLockPdaDataSynchronizationProcess(
        LockPdaDataSynchronizationInfo lockPdaDataSynchronizationInfo) {
        LockPdaDataSynchronizationProcess process = new LockPdaDataSynchronizationProcess();
        process.setSynchronizationId(lockPdaDataSynchronizationInfo.getId());
        process.setCreateTime(new Date());
        process.setStatus(PdaDataSynchronizationStatusType.START.getValue());
        return process;
    }
}
