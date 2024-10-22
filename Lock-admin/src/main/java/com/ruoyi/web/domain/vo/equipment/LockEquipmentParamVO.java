package com.ruoyi.web.domain.vo.equipment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.web.domain.vo.PageVO;
import java.util.Date;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date queryStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date queryEndDate;
}
