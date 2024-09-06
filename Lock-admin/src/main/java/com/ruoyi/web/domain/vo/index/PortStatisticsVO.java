package com.ruoyi.web.domain.vo.index;

import lombok.Data;

@Data
public class PortStatisticsVO {

    private Integer siteId;
    private String siteName;
    private Integer lockStatus;
    private Integer deploymentStatus;
    private String userCode;
}
