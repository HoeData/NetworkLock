package com.ruoyi.web.domain.vo.index;

import lombok.Data;

@Data
public class PortStatisticsChartVO {

    private Integer siteId;
    private String siteName;
    private Integer sumTotal;
    private Integer controlledTotal;
    private Double controlledProportion = 0.00;

}
