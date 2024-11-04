package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.web.domain.LockCabinet;
import com.ruoyi.web.domain.LockEquipmentModel;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.mapper.LockEquipmentModelMapper;
import com.ruoyi.web.service.ILockEquipmentModelService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class LockEquipmentModelServiceImpl extends
    ServiceImpl<LockEquipmentModelMapper, LockEquipmentModel> implements ILockEquipmentModelService {

    @Resource
    private LockEquipmentModelMapper lockEquipmentModelMapper;

    @Override
    public List<LockEquipmentModel> selectEquipmentModelList(LockCommonParamVO lockCommonParamVO) {
        return lockEquipmentModelMapper.selectEquipmentModelList(lockCommonParamVO);
    }

    @Override
    public int deleteByIds(String[] ids) {
        return lockEquipmentModelMapper.deleteByIds(ids);
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
        if (null != old) {
            if (!old.getId().equals(lockEquipmentModel.getId())) {
                throw new ServiceException("设备型号已存在");
            }
        }
    }

}
