package com.ruoyi.web.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.web.domain.LockMachineRoom;
import com.ruoyi.web.domain.LockSite;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.domain.vo.importvo.ImportMachineRoomVO;
import com.ruoyi.web.service.ILockMachineRoomService;
import com.ruoyi.web.service.ILockSiteService;
import com.ruoyi.web.utils.CommonUtils;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;


public class ImportMachineRoomListener implements ReadListener<ImportMachineRoomVO> {

    private List<LockMachineRoom> cachedDataList = Lists.newArrayList();
    private List<ImportMachineRoomVO> importMachineRoomVOList = Lists.newArrayList();
    private Map<String, Map<String, Integer>> judgeMap = Maps.newHashMap();
    private Set<String> machineRoomNameSet = Sets.newHashSet();
    private String uuid;

    public ImportMachineRoomListener() {
        initJudgeMap();
    }


    public String getUuid() {
        return uuid;
    }

    @Override
    public void invoke(ImportMachineRoomVO importMachineRoomVO, AnalysisContext analysisContext) {
        importMachineRoomVO.setErrorMsg("");
        String errorMsg = "";
        if (StringUtils.isBlank(importMachineRoomVO.getMachineRoomName())) {
            errorMsg += "机房名称不能为空,";
        }
        if (StringUtils.isBlank(importMachineRoomVO.getSiteName())) {
            errorMsg += "所属站点不能为空,";
        }
        if (StringUtils.isBlank(errorMsg)) {
            if (!judgeMap.get("siteMap").containsKey(importMachineRoomVO.getSiteName())) {
                errorMsg += "所属站点错误,";
            } else {
                importMachineRoomVO.setSiteId(
                    judgeMap.get("siteMap").get(importMachineRoomVO.getSiteName()));
                if (machineRoomNameSet.contains(importMachineRoomVO.getMachineRoomName() + ";"
                    + importMachineRoomVO.getSiteId())) {
                    errorMsg += "机房已存在,";
                } else {
                    machineRoomNameSet.add(importMachineRoomVO.getMachineRoomName() + ";"
                        + importMachineRoomVO.getSiteId());
                }
            }
        }
        if (StringUtils.isNotBlank(errorMsg)) {
            uuid = UUID.randomUUID().toString();
            importMachineRoomVO.setErrorMsg(errorMsg.substring(0, errorMsg.length() - 1));
        }
        importMachineRoomVOList.add(importMachineRoomVO);
        if (StringUtils.isBlank(uuid)) {
            LockMachineRoom lockMachineRoom = new LockMachineRoom();
            lockMachineRoom.setName(importMachineRoomVO.getMachineRoomName());
            lockMachineRoom.setDescription(importMachineRoomVO.getMachineRoomDescription());
            lockMachineRoom.setSiteId(importMachineRoomVO.getSiteId());
            CommonUtils.addCommonParams(lockMachineRoom);
            cachedDataList.add(lockMachineRoom);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if (StringUtils.isNotBlank(uuid)) {
            ImportErrorCache.errorMap.put(uuid, importMachineRoomVOList);
            return;
        }
        SpringUtils.getBean(ILockMachineRoomService.class).saveBatch(cachedDataList);
    }

    private void initJudgeMap() {
        List<LockSite> siteList = SpringUtils.getBean(ILockSiteService.class)
            .getAll(new LockCommonParamVO());
        Map<String, Integer> siteMap = siteList.stream()
            .collect(Collectors.toMap(LockSite::getName, LockSite::getId, (a, b) -> b));
        judgeMap.put("siteMap", siteMap);
        List<LockMachineRoom> MachineRoomList = SpringUtils.getBean(ILockMachineRoomService.class)
            .getAll(new LockCommonParamVO());
        for (LockMachineRoom lockMachineRoom : MachineRoomList) {
            machineRoomNameSet.add(lockMachineRoom.getName() + ";" + lockMachineRoom.getSiteId());
        }
    }
}
