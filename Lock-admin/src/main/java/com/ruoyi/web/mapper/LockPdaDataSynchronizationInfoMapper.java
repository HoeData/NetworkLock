package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.domain.LockPdaDataSynchronizationInfo;

public interface LockPdaDataSynchronizationInfoMapper extends
    BaseMapper<LockPdaDataSynchronizationInfo> {

    LockPdaDataSynchronizationInfo getLastByDeviceId(String deviceId);
}
