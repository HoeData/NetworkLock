package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.domain.LockMonitorFlow;
import com.ruoyi.web.domain.vo.LockMonitorFlowPageParamVO;
import com.ruoyi.web.mapper.LockMonitorFlowMapper;
import com.ruoyi.web.service.ILockMonitorFlowService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LockMonitorFlowServiceImpl extends
    ServiceImpl<LockMonitorFlowMapper, LockMonitorFlow> implements ILockMonitorFlowService {

    private final LockMonitorFlowMapper lockMonitorFlowMapper;

    @Override
    public LockMonitorFlow getLastForPortId(Integer portInfoId) {
        return null;
    }

    @Override
    public List<LockMonitorFlow> selectMonitorFlowList(LockMonitorFlowPageParamVO vo) {
        return lockMonitorFlowMapper.selectMonitorFlowList(vo);
    }

    @Override
    public List<Map<String, String>> getWeekTrend(LockMonitorFlowPageParamVO vo) {
//        LocalDate now = LocalDate.now();
        LocalDate now = LocalDate.of(2024, 9, 1);
        int inOutType = null != vo.getInOutType() ? vo.getInOutType() : 1;
        List<LockMonitorFlow> list = getLastOneGroupByDate(now.minusDays(6), now,
            vo.getPortInfoId());
        List<Map<String, String>> resultList = new ArrayList<>();
        Map<String, LockMonitorFlow> map = list.stream().collect(Collectors.toMap(
            lockMonitorFlow -> DateFormatUtils.format(lockMonitorFlow.getCreateTime(),
                "yyyy-MM-dd"), lockMonitorFlow -> lockMonitorFlow, (a, b) -> b));
        for (int i = 0; i < 7; i++) {
            Map<String, String> trendMap = new HashMap<>();
            String dataStr = now.minusDays(i).toString();
            trendMap.put("date", dataStr);
            trendMap.put("flow", "0");
            if (map.containsKey(dataStr)) {
                trendMap.put("flow", inOutType == 1 ? map.get(dataStr).getInFlow()
                    : map.get(dataStr).getOutFlow());
            }
            resultList.add(trendMap);
        }
        return resultList;
    }


    private List<LockMonitorFlow> getLastOneGroupByDate(LocalDate queryStart, LocalDate queryEnd,
        Integer portInfoId) {
        return lockMonitorFlowMapper.selectLastOneGroupByDate(queryStart, queryEnd, portInfoId);
    }


}
