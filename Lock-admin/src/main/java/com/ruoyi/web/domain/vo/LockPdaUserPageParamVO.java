package com.ruoyi.web.domain.vo;

import com.ruoyi.common.core.page.PageDomain;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LockPdaUserPageParamVO extends PageDomain {

    @NotBlank(message = "唯一标识不能为空")
    private Integer pdaId;
    private String userName;

}
