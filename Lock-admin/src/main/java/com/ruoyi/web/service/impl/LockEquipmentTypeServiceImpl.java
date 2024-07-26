package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
}
