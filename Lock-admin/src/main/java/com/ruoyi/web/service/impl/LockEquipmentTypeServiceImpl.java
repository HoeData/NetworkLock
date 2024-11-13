package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.web.domain.LockEquipment;
import com.ruoyi.web.domain.LockEquipmentType;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.mapper.LockEquipmentTypeMapper;
import com.ruoyi.web.service.ILockEquipmentTypeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LockEquipmentTypeServiceImpl extends
    ServiceImpl<LockEquipmentTypeMapper, LockEquipmentType> implements ILockEquipmentTypeService {
    private final LockEquipmentServiceImpl lockEquipmentService;
    private final LockEquipmentTypeMapper lockEquipmentTypeMapper;

    @Override
    public List<LockEquipmentType> selectEquipmentTypeList(LockCommonParamVO lockCommonParamVO) {
        return lockEquipmentTypeMapper.selectEquipmentTypeList(lockCommonParamVO);
    }

    @Override
    public int deleteByIds(String[] ids) {
        judgeDelete(ids);
        return lockEquipmentTypeMapper.deleteByIds(ids);
    }

    private void judgeDelete(String[] ids) {
        for (String id : ids) {
            LambdaQueryWrapper<LockEquipment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(LockEquipment::getEquipmentTypeId, id);
            lambdaQueryWrapper.eq(LockEquipment::getDelFlag, 0);
            if (lockEquipmentService.count(lambdaQueryWrapper) > 0) {
                throw new ServiceException("删除设备类型存在绑定设备,无法删除");
            }
        }
    }

    @Override
    public void judgeName(LockEquipmentType lockEquipmentType) {
        LambdaQueryWrapper<LockEquipmentType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LockEquipmentType::getName, lockEquipmentType.getName());
        wrapper.eq(LockEquipmentType::getDelFlag, 0);
        LockEquipmentType old = getOne(wrapper);
        if (null != old && null == lockEquipmentType.getId()) {
            throw new ServiceException("设备类型已存在");
        }
        if (null != old && !old.getId().equals(lockEquipmentType.getId())) {
            throw new ServiceException("设备类型已存在");
        }
    }

}
