package com.ruoyi.web.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;

@Data
public class LockWarnInfoViewVO {

    private String ip;
    private String community;
    private String companyName;
    private String deptName;
    private String siteName;
    private String equipmentName;
    private String machineRoomName;
    private String cabinetName;
    private String equipmentTypeName;
    private String equipmentModelName;
    private Integer portInfoId;
    private Integer portIndex;
    private Integer warnInfoId;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
