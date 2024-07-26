package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
}
