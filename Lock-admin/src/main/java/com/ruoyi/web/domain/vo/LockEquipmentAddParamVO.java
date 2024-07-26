package com.ruoyi.web.domain.vo;

import lombok.Data;

@Data
public class LockEquipmentAddParamVO extends LockEntity{
    private Integer id;
    private Integer deptId;
    private Integer cabinetId;
    private Integer equipmentTypeId;
    private Integer equipmentModelId;
    private String name;
    private String description;
    private String delFlag;
    private Integer consoleFlag;
    private Integer portNumber;



}
