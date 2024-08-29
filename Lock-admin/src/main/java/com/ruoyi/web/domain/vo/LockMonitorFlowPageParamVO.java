package com.ruoyi.web.domain.vo;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LockMonitorFlowPageParamVO extends PageVO {

    @NotNull(message = "网口不能为空")
    private Integer portInfoId;

}
