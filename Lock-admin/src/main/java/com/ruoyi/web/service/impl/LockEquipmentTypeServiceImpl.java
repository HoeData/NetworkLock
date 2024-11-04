package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.web.domain.LockEquipmentModel;
import com.ruoyi.web.domain.LockEquipmentType;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.mapper.LockEquipmentTypeMapper;
import com.ruoyi.web.service.ILockEquipmentTypeService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class LockEquipmentTypeServiceImpl extends
    ServiceImpl<LockEquipmentTypeMapper, LockEquipmentType> implements ILockEquipmentTypeService {

    @Resource
    private LockEquipmentTypeMapper lockEquipmentTypeMapper;

    @Override
    public List<LockEquipmentType> selectEquipmentTypeList(LockCommonParamVO lockCommonParamVO) {
        return lockEquipmentTypeMapper.selectEquipmentTypeList(lockCommonParamVO);
    }

    @Override
    public int deleteByIds(String[] ids) {
        return lockEquipmentTypeMapper.deleteByIds(ids);
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
        if (null != old) {
            if (!old.getId().equals(lockEquipmentType.getId())) {
                throw new ServiceException("设备类型已存在");
            }
        }
    }

}
