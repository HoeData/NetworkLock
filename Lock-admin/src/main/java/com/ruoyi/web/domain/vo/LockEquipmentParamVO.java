package com.ruoyi.web.domain.vo;

import lombok.Data;

@Data
public class LockEquipmentParamVO extends PageVO{

    private Integer equipmentId;
    private String name;
    private Integer companyId;
    private Integer deptId;
    private Integer machineRoomId;
    private Integer cabinetId;
    private Integer equipmentTypeId;
    private Integer equipmentModelId;
    private String description;


}