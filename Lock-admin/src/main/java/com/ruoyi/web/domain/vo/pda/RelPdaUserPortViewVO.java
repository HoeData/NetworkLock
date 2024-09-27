package com.ruoyi.web.domain.vo.pda;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;

@Data
public class RelPdaUserPortViewVO {

    private String companyName;
    private String deptName;
    private String siteName;
    private String machineRoomName;
    private String cabinetName;
    private String equipmentTypeName;
    private String equipmentModelName;
    private String equipmentName;
    private Integer equipmentId;

    private Integer portInfoId;
    private Integer portInfoIndex;
    private String portInfoUserCode;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date validityPeriod;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


}
