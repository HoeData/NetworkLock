package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.web.domain.LockEquipmentType;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import java.util.List;

public interface ILockEquipmentTypeService extends IService<LockEquipmentType> {


    List<LockEquipmentType> selectEquipmentTypeList(LockCommonParamVO lockCommonParamVO);

    int deleteByIds(String[] ids);

    void judgeName(LockEquipmentType lockEquipmentType);

    List<LockEquipmentType> getAll();
}
