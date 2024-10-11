package com.ruoyi.web.domain.vo.pda;

import com.ruoyi.web.domain.vo.PageVO;
import lombok.Data;

@Data
public class UnlockPageParamVO extends PageVO {

    private Integer successFlag;
    private String userName;
    private Integer status;
    private String lockSerialNumber;
}
