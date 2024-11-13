package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.web.domain.LockPdaDataSynchronizationProcess;
import java.util.List;

public interface ILockPdaDataSynchronizationProcessService extends
    IService<LockPdaDataSynchronizationProcess> {

    List<LockPdaDataSynchronizationProcess> selectBySynchronizationId(Integer synchronizationId);
}
