package com.manniu.offline.domain;

import lombok.Data;

@Data
public class LockEquipment extends LockEntity {

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
    private String ip;
    private String community;
    private Integer activeDefenseFlag;
}
