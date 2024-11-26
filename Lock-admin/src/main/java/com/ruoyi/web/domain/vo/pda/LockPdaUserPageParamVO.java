package com.ruoyi.web.domain.vo.pda;

import com.ruoyi.common.core.page.PageDomain;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LockPdaUserPageParamVO extends PageDomain {

    @NotNull(message = "唯一标识不能为空")
    private Integer pdaId;
    private String userName;

}
