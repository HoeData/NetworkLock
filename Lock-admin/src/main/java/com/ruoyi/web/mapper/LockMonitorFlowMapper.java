package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.domain.LockMonitorFlow;
import com.ruoyi.web.domain.vo.LockMonitorFlowPageParamVO;
import java.util.List;

public interface LockMonitorFlowMapper extends BaseMapper<LockMonitorFlow> {


    List<LockMonitorFlow> selectMonitorFlowList(LockMonitorFlowPageParamVO vo);
}
