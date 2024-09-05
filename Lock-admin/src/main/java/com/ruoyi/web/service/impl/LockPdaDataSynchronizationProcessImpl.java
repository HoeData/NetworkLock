package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.domain.LockPdaDataSynchronizationProcess;
import com.ruoyi.web.mapper.LockPdaDataSynchronizationProcessMapper;
import com.ruoyi.web.service.ILockPdaDataSynchronizationProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LockPdaDataSynchronizationProcessImpl extends
    ServiceImpl<LockPdaDataSynchronizationProcessMapper, LockPdaDataSynchronizationProcess> implements
    ILockPdaDataSynchronizationProcessService {

}
