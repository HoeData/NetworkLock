package com.ruoyi.web.service;

import com.ruoyi.web.domain.vo.index.PortStatisticsChartVO;
import com.ruoyi.web.domain.vo.index.PortStatisticsVO;
import com.ruoyi.web.domain.vo.index.Recent12MonthsStatisticsVO;
import com.ruoyi.web.mapper.LockPortInfoMapper;
import com.ruoyi.web.mapper.LockUnlockLogMapper;
import com.ruoyi.web.mapper.LockWarnInfoMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LockIndexService {

    private final LockPortInfoMapper lockPortInfoMapper;
    private final LockWarnInfoMapper warnInfoMapper;
    private final LockUnlockLogMapper lockUnlockLogMapper;

    public Map<String, Object> getIndexStatisticalQuantity() {
        Map<String, Object> resultMap = new HashMap<>();
        List<PortStatisticsChartVO> controlledTotalStatisticsList = setPortStatistics();
        resultMap.put("controlledTotalStatistics", controlledTotalStatisticsList);
        resultMap.put("warnRecent12MonthsTotalStatistics", getRecent12MonthsStatistics(1));
        resultMap.put("unlockRecent12MonthsTotalStatistics", getRecent12MonthsStatistics(2));
        Map<String,Integer> lockTotalStatistics = new HashMap<>();
        lockTotalStatistics.put("lockTotal", lockPortInfoMapper.selectLockPortTotal());
        lockTotalStatistics.put("idleTotal",lockPortInfoMapper.selectIdleTotal());
        lockTotalStatistics.put("useTotal", lockPortInfoMapper.selectUseTotal());
        resultMap.put("lockTotalStatistics", lockTotalStatistics);
        return resultMap;
    }

    private List<PortStatisticsChartVO> setPortStatistics() {
        List<PortStatisticsVO> portStatisticsVOList = lockPortInfoMapper.selectPortStatisticsVOList();
        Map<Integer, Integer> sitePortSumMap = new HashMap<>();
        Map<Integer, String> siteIdNameMap = new HashMap<>();
        Map<Integer, Integer> controlledTotalMap = new HashMap<>();
        for (PortStatisticsVO vo : portStatisticsVOList) {
            sitePortSumMap.put(vo.getSiteId(),
                sitePortSumMap.getOrDefault(vo.getSiteId(), 0) + 1);
            siteIdNameMap.put(vo.getSiteId(), vo.getSiteName());
            if (vo.getLockStatus() == 1) {
                controlledTotalMap.put(vo.getSiteId(),
                    controlledTotalMap.getOrDefault(vo.getSiteId(), 0) + 1);
            }
        }
        List<PortStatisticsChartVO> controlledTotalStatisticsList = new ArrayList<>();
        sitePortSumMap.forEach((k, v) -> {
            PortStatisticsChartVO portStatisticsChartVO = new PortStatisticsChartVO();
            portStatisticsChartVO.setSiteId(k);
            portStatisticsChartVO.setSiteName(siteIdNameMap.get(k));
            portStatisticsChartVO.setSumTotal(v);
            portStatisticsChartVO.setControlledTotal(controlledTotalMap.getOrDefault(k, 0));
            if (portStatisticsChartVO.getSumTotal() != 0) {
                portStatisticsChartVO.setControlledProportion(
                    (double) portStatisticsChartVO.getControlledTotal()
                        / portStatisticsChartVO.getSumTotal());
            }
            controlledTotalStatisticsList.add(portStatisticsChartVO);
        });
        return controlledTotalStatisticsList;
    }


    private List<Recent12MonthsStatisticsVO> getRecent12MonthsStatistics(Integer type) {
        List<Recent12MonthsStatisticsVO> warnInfoStatisticsVOList = new ArrayList<>();
        LocalDate currentDate = LocalDate.now().withDayOfMonth(1);
        for (int i = 0; i < 12; i++) {
            LocalDate monthDate = currentDate.minusMonths(i);
            Recent12MonthsStatisticsVO warnInfoStatisticsVO;
            if (type == 1) {
                warnInfoStatisticsVO = warnInfoMapper.selectTotalByStartAndEndDate(
                    currentDate.minusMonths(i), monthDate.plusMonths(1));
            } else {
                warnInfoStatisticsVO = lockUnlockLogMapper.selectTotalByStartAndEndDate(
                    currentDate.minusMonths(i), monthDate.plusMonths(1));
            }
            if (null == warnInfoStatisticsVO) {
                warnInfoStatisticsVO = new Recent12MonthsStatisticsVO();
                warnInfoStatisticsVO.setWarnInfoTotal(0);
            }
            warnInfoStatisticsVO.setDate(monthDate);
            warnInfoStatisticsVOList.add(warnInfoStatisticsVO);
        }
        return warnInfoStatisticsVOList;
    }

}
