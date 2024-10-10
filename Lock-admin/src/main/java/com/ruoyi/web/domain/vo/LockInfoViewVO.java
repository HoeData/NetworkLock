package com.ruoyi.web.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;

@Data
public class LockInfoViewVO {

    private String serialNumber;
    private String batchNo;
    private String companyName;
    private String deptName;
    private String machineRoomName;
    private String cabinetName;
    private String equipmentName;
    private String equipmentTypeName;
    private String equipmentModelName;
    private String siteName;
    private Integer portIndex;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private Integer deploymentStatus;

    private Integer type;

}
