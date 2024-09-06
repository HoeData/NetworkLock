package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.domain.LockUnlockLog;
import com.ruoyi.web.domain.vo.index.Recent12MonthsStatisticsVO;
import java.time.LocalDate;
import org.apache.ibatis.annotations.Param;

public interface LockUnlockLogMapper extends BaseMapper<LockUnlockLog> {

    Recent12MonthsStatisticsVO selectTotalByStartAndEndDate(@Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);
}
