package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.domain.LockMonitorFlow;
import com.ruoyi.web.domain.vo.LockMonitorFlowPageParamVO;
import java.time.LocalDate;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface LockMonitorFlowMapper extends BaseMapper<LockMonitorFlow> {

    LockMonitorFlow selectLastForPortId(Integer portInfoId);

    List<LockMonitorFlow> selectMonitorFlowList(LockMonitorFlowPageParamVO vo);

    List<LockMonitorFlow> selectLastOneGroupByDate(
        @Param("queryStartTime") LocalDate queryStartTime,
        @Param("queryEndTime") LocalDate queryEndTime, @Param("portInfoId") Integer portInfoId);
}
