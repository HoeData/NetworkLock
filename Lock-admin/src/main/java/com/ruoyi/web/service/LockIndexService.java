package com.ruoyi.web.service;

import com.ruoyi.web.annotation.CompanyScope;
import com.ruoyi.web.domain.LockPortInfo;
import com.ruoyi.web.domain.LockSite;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.domain.vo.equipment.LockEquipmentParamVO;
import com.ruoyi.web.domain.vo.equipment.LockEquipmentViewVO;
import com.ruoyi.web.domain.vo.index.PortStatisticsChartVO;
import com.ruoyi.web.domain.vo.index.PortStatisticsVO;
import com.ruoyi.web.domain.vo.index.Recent12MonthsStatisticsVO;
import com.ruoyi.web.enums.QueryLatitudeType;
import com.ruoyi.web.mapper.LockEquipmentMapper;
import com.ruoyi.web.mapper.LockPortInfoMapper;
import com.ruoyi.web.mapper.LockUnlockLogMapper;
import com.ruoyi.web.mapper.LockWarnInfoMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LockIndexService {

    private final LockPortInfoMapper lockPortInfoMapper;
    private final LockWarnInfoMapper warnInfoMapper;
    private final LockUnlockLogMapper lockUnlockLogMapper;
    private final ILockSiteService lockSiteService;
    private final LockEquipmentMapper lockEquipmentMapper;

    @CompanyScope
    public Map<String, Object> getIndexStatisticalQuantity(LockCommonParamVO vo) {
        Map<String, Object> resultMap = new HashMap<>();
        List<PortStatisticsChartVO> controlledTotalStatisticsList = setPortStatistics();
        resultMap.put("controlledTotalStatistics", controlledTotalStatisticsList);
        resultMap.put("warnRecent12MonthsTotalStatistics", getRecent12MonthsStatistics(1));
        resultMap.put("unlockRecent12MonthsTotalStatistics", getRecent12MonthsStatistics(2));
        Map<String,Integer> lockTotalStatistics = new HashMap<>();
        lockTotalStatistics.put("lockTotal", lockPortInfoMapper.selectLockPortTotal(vo));
        lockTotalStatistics.put("idleTotal",lockPortInfoMapper.selectIdleTotal(vo));
        lockTotalStatistics.put("useTotal", lockPortInfoMapper.selectUseTotal(vo));
        resultMap.put("lockTotalStatistics", lockTotalStatistics);
        return resultMap;
    }

    private List<PortStatisticsChartVO> setPortStatistics() {
        List<PortStatisticsChartVO> controlledTotalStatisticsList = new ArrayList<>();
        List<LockSite> lockSiteList = lockSiteService.getAll(new LockCommonParamVO());
        if(lockSiteList.size()==0){
            return controlledTotalStatisticsList;
        }
        List<PortStatisticsVO> portStatisticsVOList = lockPortInfoMapper.selectPortStatisticsVOList();
        Map<Integer, Integer> sitePortSumMap = new HashMap<>();
        Map<Integer, String> siteIdNameMap = new HashMap<>();
        Map<Integer, Integer> controlledTotalMap = new HashMap<>();
        for (PortStatisticsVO vo : portStatisticsVOList) {
            sitePortSumMap.put(vo.getSiteId(),
                sitePortSumMap.getOrDefault(vo.getSiteId(), 0) + 1);
            siteIdNameMap.put(vo.getSiteId(), vo.getSiteName());
            if (vo.getDeploymentStatus() <3) {
                controlledTotalMap.put(vo.getSiteId(),
                    controlledTotalMap.getOrDefault(vo.getSiteId(), 0) + 1);
            }
        }
        lockSiteList.forEach(lockSite -> {
            PortStatisticsChartVO portStatisticsChartVO = new PortStatisticsChartVO();
            portStatisticsChartVO.setSiteId(lockSite.getId());
            portStatisticsChartVO.setSiteName(lockSite.getName());
            portStatisticsChartVO.setSumTotal(sitePortSumMap.getOrDefault(lockSite.getId(), 0));
            portStatisticsChartVO.setControlledTotal(controlledTotalMap.getOrDefault(lockSite.getId(), 0));
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

    public List<Map<String, Object>> getLockNumberByStatusAndSite() {
        List<Map<String, Object>> list = new ArrayList<>();
        List<LockSite> lockSiteList = lockSiteService.getAll(new LockCommonParamVO());
        if(lockSiteList.size()==0){
            return list;
        }
        List<PortStatisticsVO> portStatisticsVOList = lockPortInfoMapper.selectPortStatisticsVOList();
        Map<Integer, String> siteIdNameMap = new HashMap<>();
        Map<Integer, Integer> useTotalMap = new HashMap<>();
        Map<Integer, Integer> idleTotalMap = new HashMap<>();
        for (PortStatisticsVO vo : portStatisticsVOList) {
            siteIdNameMap.put(vo.getSiteId(), vo.getSiteName());
                if (vo.getDeploymentStatus() == 1) {
                    idleTotalMap.put(vo.getSiteId(),
                        idleTotalMap.getOrDefault(vo.getSiteId(), 1) + 1);
                } else if (vo.getDeploymentStatus() == 2) {
                    useTotalMap.put(vo.getSiteId(),
                        useTotalMap.getOrDefault(vo.getSiteId(), 1) + 1);
                }
        }
        lockSiteList.forEach(lockSite -> {
            Map<String, Object> numberMap = new HashMap<>();
            numberMap.put("idleTotal", idleTotalMap.getOrDefault(lockSite.getId(), 0));
            numberMap.put("useTotal", useTotalMap.getOrDefault(lockSite.getId(), 0));
            numberMap.put("siteName", lockSite.getName());
            numberMap.put("siteId", lockSite.getId());
            numberMap.put("latitude", lockSite.getLatitude());
            numberMap.put("longitude", lockSite.getLongitude());
            list.add(numberMap);
        });
        return list;
    }

    public Map<String, Object> getLockStatusListByLatitudeType(String type, Integer value) {
        LockEquipmentParamVO lockEquipmentParamVO = new LockEquipmentParamVO();
        QueryLatitudeType.setField(lockEquipmentParamVO, type, value);
        List<LockEquipmentViewVO> list = lockEquipmentMapper.selectEquipmentList(
            lockEquipmentParamVO);
        List<Integer> equipmentIdList = list.stream().map(LockEquipmentViewVO::getId).collect(
            Collectors.toList());
        List<LockPortInfo> portInfoList = new ArrayList<>();
        if (equipmentIdList.size() > 0) {
            portInfoList = lockPortInfoMapper.selectListByEquipmentIds(
                equipmentIdList);
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("equipmentTotal", list.size());
        resultMap.put("allPortTotal", portInfoList.size());
        int blockedPortTotal = 0;
        int idleTotal = 0;
        int useTotal = 0;
        int consoleTotal = 0;
        for (LockPortInfo portInfo : portInfoList) {
            if (portInfo.getDeploymentStatus() == 1) {
                idleTotal++;
            } else if (portInfo.getDeploymentStatus() == 2) {
                useTotal++;
            } else if (portInfo.getDeploymentStatus() == 4) {
                blockedPortTotal++;
            }
            if (portInfo.getSerialNumber().equals("console口")) {
                consoleTotal++;
            }
        }
        resultMap.put("blockedPortTotal", blockedPortTotal);
        resultMap.put("idleTotal", idleTotal);
        resultMap.put("useTotal", useTotal);
        resultMap.put("consoleTotal", consoleTotal);
        return resultMap;
    }
}
