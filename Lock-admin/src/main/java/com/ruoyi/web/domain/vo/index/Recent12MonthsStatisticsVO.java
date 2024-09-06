package com.ruoyi.web.domain.vo.index;

import java.time.LocalDate;
import lombok.Data;

@Data
public class Recent12MonthsStatisticsVO {
    private LocalDate date;
    private Integer warnInfoTotal;
}
