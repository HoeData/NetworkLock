package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.web.domain.LockMonitorFlow;
import com.ruoyi.web.domain.LockWarnInfo;
import com.ruoyi.web.domain.vo.LockMonitorFlowPageParamVO;
import java.util.List;

public interface ILockMonitorFlowService extends IService<LockMonitorFlow> {

    List<LockMonitorFlow> selectMonitorFlowList(LockMonitorFlowPageParamVO vo);
}
