package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.domain.LockMonitorFlow;
import com.ruoyi.web.domain.vo.LockMonitorFlowPageParamVO;
import com.ruoyi.web.mapper.LockMonitorFlowMapper;
import com.ruoyi.web.mapper.LockWarnInfoMapper;
import com.ruoyi.web.service.ILockMonitorFlowService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LockMonitorFlowServiceImpl extends
    ServiceImpl<LockMonitorFlowMapper, LockMonitorFlow> implements ILockMonitorFlowService {

    private final LockMonitorFlowMapper lockMonitorFlowMapper;

    @Override
    public List<LockMonitorFlow> selectMonitorFlowList(LockMonitorFlowPageParamVO vo) {
        return lockMonitorFlowMapper.selectMonitorFlowList(vo);
    }
}
