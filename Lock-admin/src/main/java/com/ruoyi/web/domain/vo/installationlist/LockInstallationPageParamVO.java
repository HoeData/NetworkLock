package com.ruoyi.web.domain.vo.installationlist;

import com.ruoyi.common.core.page.PageDomain;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LockInstallationPageParamVO extends PageDomain {

    @NotNull(message = "站点不能为空")
    private Integer siteId;
}
