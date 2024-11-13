package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.web.domain.LockEquipment;
import com.ruoyi.web.domain.LockEquipmentModel;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.mapper.LockEquipmentModelMapper;
import com.ruoyi.web.service.ILockEquipmentModelService;
import com.ruoyi.web.service.ILockEquipmentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LockEquipmentModelServiceImpl extends
    ServiceImpl<LockEquipmentModelMapper, LockEquipmentModel> implements
    ILockEquipmentModelService {

    private final LockEquipmentModelMapper lockEquipmentModelMapper;

    private final ILockEquipmentService lockEquipmentService;

    @Override
    public List<LockEquipmentModel> selectEquipmentModelList(LockCommonParamVO lockCommonParamVO) {
        return lockEquipmentModelMapper.selectEquipmentModelList(lockCommonParamVO);
    }

    @Override
    public int deleteByIds(String[] ids) {
        judgeDelete(ids);
        return lockEquipmentModelMapper.deleteByIds(ids);
    }

    private void judgeDelete(String[] ids) {
        for (String id : ids) {
            LambdaQueryWrapper<LockEquipment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(LockEquipment::getEquipmentModelId, id);
            lambdaQueryWrapper.eq(LockEquipment::getDelFlag, 0);
            if (lockEquipmentService.count(lambdaQueryWrapper) > 0) {
                throw new ServiceException("删除设备型号存在绑定设备,无法删除");
            }
        }
    }

    @Override
    public void judgeName(LockEquipmentModel lockEquipmentModel) {
        LambdaQueryWrapper<LockEquipmentModel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LockEquipmentModel::getName, lockEquipmentModel.getName());
        wrapper.eq(LockEquipmentModel::getDelFlag, 0);
        LockEquipmentModel old = getOne(wrapper);
        if (null != old && null == lockEquipmentModel.getId()) {
            throw new ServiceException("设备型号已存在");
        }
        if (null != old && !old.getId().equals(lockEquipmentModel.getId())) {
            throw new ServiceException("设备型号已存在");
        }
    }

}
