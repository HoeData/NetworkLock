package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.web.domain.LockPdaDataSynchronizationInfo;
import com.ruoyi.web.enums.PdaDataSynchronizationType;

public interface ILockPdaDataSynchronizationInfoService extends
    IService<LockPdaDataSynchronizationInfo> {

    LockPdaDataSynchronizationInfo saveAll(String deviceId,
        PdaDataSynchronizationType pdaDataSynchronizationType);

    void updateStatus(LockPdaDataSynchronizationInfo lockPdaDataSynchronizationInfo, Integer status);

    LockPdaDataSynchronizationInfo getLastByDeviceId(String deviceId);
}
