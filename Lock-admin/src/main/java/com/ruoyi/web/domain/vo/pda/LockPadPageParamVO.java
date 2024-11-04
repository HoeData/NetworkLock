package com.ruoyi.web.domain.vo.pda;

import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.web.domain.vo.PageVO;
import lombok.Data;

@Data
public class LockPadPageParamVO extends PageVO {

    private Integer type;
    private String  onlyKey;
    private String description;

}
