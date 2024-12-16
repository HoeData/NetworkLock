package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.web.domain.LockEquipment;
import com.ruoyi.web.domain.vo.equipment.ActiveDefenseSaveOrUpdateParamVO;
import com.ruoyi.web.domain.vo.equipment.LockEquipmentAddParamVO;
import com.ruoyi.web.domain.vo.equipment.LockEquipmentParamVO;
import com.ruoyi.web.domain.vo.equipment.LockEquipmentViewVO;
import java.util.List;

public interface ILockEquipmentService extends IService<LockEquipment> {


    List<LockEquipmentViewVO> selectEquipmentList(LockEquipmentParamVO lockEquipmentParamVO);

    int deleteByIds(String[] ids);

    int add(LockEquipmentAddParamVO lockEquipmentAddParamVO);
    int update(LockEquipmentAddParamVO lockEquipmentAddParamVO);
    void judgeName(Integer id, String name, Integer cabinetId);

    int setActiveDefenseFlag(ActiveDefenseSaveOrUpdateParamVO vo);

    int updateActiveDefenseFlag(ActiveDefenseSaveOrUpdateParamVO vo);

    int removeActiveDefenseByIds(String[] ids);
    List<LockEquipment> getAll(LockEquipmentParamVO lockEquipmentParamVO);
    void saveOrUpdateForSynchronization(List<LockEquipment> list);
}
