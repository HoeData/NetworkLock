package com.ruoyi.web.domain.vo;

import lombok.Data;

@Data
public class LockInfoPageParamVO extends PageVO {
    private String serialNumber;
    private String batchNo;
    private Integer type;
}
