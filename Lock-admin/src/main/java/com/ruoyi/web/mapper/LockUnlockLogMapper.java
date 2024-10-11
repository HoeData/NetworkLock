package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.domain.LockUnlockLog;
import com.ruoyi.web.domain.vo.PageVO;
import com.ruoyi.web.domain.vo.index.Recent12MonthsStatisticsVO;
import com.ruoyi.web.domain.vo.pda.LockUnlockViewVO;
import com.ruoyi.web.domain.vo.pda.UnlockPageParamVO;
import java.time.LocalDate;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface LockUnlockLogMapper extends BaseMapper<LockUnlockLog> {

    Recent12MonthsStatisticsVO selectTotalByStartAndEndDate(@Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);

    List<LockUnlockViewVO> selectUnlockList(UnlockPageParamVO pageVO);
}
