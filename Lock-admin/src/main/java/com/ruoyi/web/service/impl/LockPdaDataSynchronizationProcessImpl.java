package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.domain.LockPdaDataSynchronizationProcess;
import com.ruoyi.web.enums.PdaDataSynchronizationStatusType;
import com.ruoyi.web.mapper.LockPdaDataSynchronizationProcessMapper;
import com.ruoyi.web.service.ILockPdaDataSynchronizationProcessService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LockPdaDataSynchronizationProcessImpl extends
    ServiceImpl<LockPdaDataSynchronizationProcessMapper, LockPdaDataSynchronizationProcess> implements
    ILockPdaDataSynchronizationProcessService {

    @Override
    public List<LockPdaDataSynchronizationProcess> selectBySynchronizationId(
        Integer synchronizationId) {
        LambdaQueryWrapper<LockPdaDataSynchronizationProcess> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LockPdaDataSynchronizationProcess::getSynchronizationId, synchronizationId);
        List<LockPdaDataSynchronizationProcess> list = list(queryWrapper);
        list.forEach(item -> {
            PdaDataSynchronizationStatusType type = PdaDataSynchronizationStatusType.getEnum(
                item.getStatus());
            if (null != type) {
                item.setStatusDesc(type.getMsg());
            }

        });
        return list;
    }
}
