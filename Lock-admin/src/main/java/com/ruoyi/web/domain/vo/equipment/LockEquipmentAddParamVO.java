package com.ruoyi.web.domain.vo.equipment;

import com.ruoyi.web.domain.vo.LockEntity;
import lombok.Data;

@Data
public class LockEquipmentAddParamVO extends LockEntity {
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
    private Integer trustFlag;
    private String ip;
    private String community;



}
