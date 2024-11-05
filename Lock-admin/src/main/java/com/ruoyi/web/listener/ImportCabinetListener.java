package com.ruoyi.web.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.web.domain.LockCabinet;
import com.ruoyi.web.domain.LockMachineRoom;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.domain.vo.importvo.ImportCabinetVO;
import com.ruoyi.web.service.ILockCabinetService;
import com.ruoyi.web.service.ILockMachineRoomService;
import com.ruoyi.web.utils.CommonUtils;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

public class ImportCabinetListener implements ReadListener<ImportCabinetVO> {

    private List<LockCabinet> cachedDataList = Lists.newArrayList();
    private List<ImportCabinetVO> importCabinetVOList = Lists.newArrayList();
    private Map<String, Map<String, Integer>> judgeMap = Maps.newHashMap();
    private Set<String> cabinetNameSet = Sets.newHashSet();
    private String uuid;

    public ImportCabinetListener() {
        initJudgeMap();
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public void invoke(ImportCabinetVO importCabinetVO, AnalysisContext analysisContext) {
        importCabinetVO.setErrorMsg("");
        String errorMsg = "";
        if (StringUtils.isBlank(importCabinetVO.getCabinetName())) {
            errorMsg += "机柜名称不能为空,";
        }
        if (StringUtils.isBlank(importCabinetVO.getMachineRoomName())) {
            errorMsg += "所属机房不能为空,";
        }
        if (StringUtils.isBlank(errorMsg)) {
            if (!judgeMap.get("machineRoomMap").containsKey(importCabinetVO.getMachineRoomName())) {
                errorMsg += "所属机房错误,";
            } else {
                importCabinetVO.setMachineRoomId(
                    judgeMap.get("machineRoomMap").get(importCabinetVO.getMachineRoomName()));
                if (cabinetNameSet.contains(
                    importCabinetVO.getCabinetName() + ";" + importCabinetVO.getMachineRoomId())) {
                    errorMsg += "机柜已存在,";
                } else {
                    cabinetNameSet.add(importCabinetVO.getCabinetName() + ";"
                        + importCabinetVO.getMachineRoomId());
                }
            }
        }
        if (StringUtils.isNotBlank(errorMsg)) {
            uuid = UUID.randomUUID().toString();
            importCabinetVO.setErrorMsg(errorMsg.substring(0, errorMsg.length() - 1));
        }
        importCabinetVOList.add(importCabinetVO);
        if (StringUtils.isBlank(uuid)) {
            LockCabinet lockCabinet = new LockCabinet();
            lockCabinet.setName(importCabinetVO.getCabinetName());
            lockCabinet.setDescription(importCabinetVO.getCabinetDescription());
            lockCabinet.setMachineRoomId(importCabinetVO.getMachineRoomId());
            CommonUtils.addCommonParams(lockCabinet);
            cachedDataList.add(lockCabinet);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if (StringUtils.isNotBlank(uuid)) {
            ImportErrorCache.errorMap.put(uuid, importCabinetVOList);
            return;
        }
        SpringUtils.getBean(ILockCabinetService.class).saveBatch(cachedDataList);
    }

    private void initJudgeMap() {
        List<LockMachineRoom> machineRoomList = SpringUtils.getBean(ILockMachineRoomService.class)
            .getAll(new LockCommonParamVO());
        Map<String, Integer> machineRoomMap = machineRoomList.stream().collect(
            Collectors.toMap(LockMachineRoom::getName, LockMachineRoom::getId, (a, b) -> b));
        judgeMap.put("machineRoomMap", machineRoomMap);
        List<LockCabinet> CabinetList = SpringUtils.getBean(ILockCabinetService.class)
            .getAll(new LockCommonParamVO());
        for (LockCabinet lockCabinet : CabinetList) {
            cabinetNameSet.add(lockCabinet.getName() + ";" + lockCabinet.getMachineRoomId());
        }
    }
}
