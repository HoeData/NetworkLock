package com.ruoyi.web.domain.vo.equipment;

import com.ruoyi.web.domain.vo.PageVO;
import lombok.Data;

@Data
public class LockEquipmentParamVO extends PageVO {

    private Integer equipmentId;
    private Integer siteId;
    private String name;
    private Integer companyId;
    private Integer deptId;
    private Integer machineRoomId;
    private Integer cabinetId;
    private Integer equipmentTypeId;
    private Integer equipmentModelId;
    private String description;

    private Integer activeDefenseFlag;


}
