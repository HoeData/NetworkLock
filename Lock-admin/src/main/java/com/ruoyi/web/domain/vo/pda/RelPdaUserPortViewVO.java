package com.ruoyi.web.domain.vo.pda;

import java.util.List;
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
    private List<RelPortViewVO> portViewList;


}
