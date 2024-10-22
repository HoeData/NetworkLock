package com.ruoyi.web.domain.vo.pda;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.web.domain.vo.PageVO;
import java.util.Date;
import lombok.Data;

@Data
public class UnlockPageParamVO extends PageVO {

    private Integer successFlag;
    private String userName;
    private Integer status;
    private String lockSerialNumber;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date queryStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date queryEndDate;
}
