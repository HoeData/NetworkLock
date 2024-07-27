package com.ruoyi.web.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;

@Data
public class LockEquipmentViewVO{

    private Integer id;
    private String name;
    private String companyName;
    private Integer deptId;
    private String deptName;
    private String machineRoomName;
    private Integer cabinetId;
    private String cabinetName;
    private Integer equipmentTypeId;
    private String equipmentTypeName;
    private Integer equipmentModelId;
    private String equipmentModelName;
    private String siteName;
    private String description;
    private String delFlag;
    private Integer portNumber;
    private Integer consoleFlag;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
