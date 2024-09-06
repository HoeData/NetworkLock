package com.ruoyi.web.domain.vo.pda;

import com.ruoyi.common.core.page.PageDomain;
import lombok.Data;

@Data
public class LockPadPageParamVO extends PageDomain {

    private Integer type;
    private String  key;
    private String description;

}
