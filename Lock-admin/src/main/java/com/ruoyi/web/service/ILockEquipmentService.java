package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.web.domain.LockEquipment;
import com.ruoyi.web.domain.vo.LockEquipmentAddParamVO;
import com.ruoyi.web.domain.vo.LockEquipmentParamVO;
import com.ruoyi.web.domain.vo.LockEquipmentViewVO;
import java.util.List;

public interface ILockEquipmentService extends IService<LockEquipment> {


    List<LockEquipmentViewVO> selectEquipmentList(LockEquipmentParamVO lockCommonParamVO);

    int deleteByIds(String[] ids);

    int add(LockEquipmentAddParamVO lockEquipmentAddParamVO);
    int update(LockEquipmentAddParamVO lockEquipmentAddParamVO);

    int setTrust(LockEquipmentAddParamVO lockEquipmentAddParamVO);

    void judgeName(Integer id, String name, Integer cabinetId);
}
