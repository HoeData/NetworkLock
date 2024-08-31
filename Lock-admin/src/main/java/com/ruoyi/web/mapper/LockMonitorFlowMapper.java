package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.domain.LockMonitorFlow;
import com.ruoyi.web.domain.vo.LockMonitorFlowPageParamVO;
import java.util.List;

public interface LockMonitorFlowMapper extends BaseMapper<LockMonitorFlow> {

    LockMonitorFlow selectLastForPortId(Integer portInfoId);
    List<LockMonitorFlow> selectMonitorFlowList(LockMonitorFlowPageParamVO vo);
}
