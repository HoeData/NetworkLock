package com.ruoyi.web.domain.vo.pda;

import com.ruoyi.common.core.page.PageDomain;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RelPdaUserPortParamVO extends PageDomain {

    private Integer companyId;
    private Integer deptId;
    private Integer siteId;
    private Integer machineRoomId;
    private Integer cabinetId;
    private Integer equipmentTypeId;
    private Integer equipmentModelId;
    private Integer equipmentId;
    @NotNull(message = "用户不能为空")
    private Integer pdaUserId;
    private String  lockSerialNumber;


}
