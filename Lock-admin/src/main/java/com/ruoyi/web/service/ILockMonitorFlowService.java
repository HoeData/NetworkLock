package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.web.domain.LockMonitorFlow;
import com.ruoyi.web.domain.vo.LockMonitorFlowPageParamVO;
import java.util.List;
import java.util.Map;

public interface ILockMonitorFlowService extends IService<LockMonitorFlow> {

    LockMonitorFlow getLastForPortId(Integer portInfoId);

    List<LockMonitorFlow> selectMonitorFlowList(LockMonitorFlowPageParamVO vo);

    Map<String,List<Map<String, String>>> getWeekTrend(LockMonitorFlowPageParamVO vo);
}
