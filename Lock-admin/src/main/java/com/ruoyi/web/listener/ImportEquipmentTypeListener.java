package com.ruoyi.web.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.web.domain.LockEquipmentType;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.domain.vo.importvo.ImportEquipmentTypeVO;
import com.ruoyi.web.service.ILockEquipmentTypeService;
import com.ruoyi.web.utils.CommonUtils;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;


public class ImportEquipmentTypeListener implements ReadListener<ImportEquipmentTypeVO> {

    private List<LockEquipmentType> cachedDataList = Lists.newArrayList();
    private List<ImportEquipmentTypeVO> importEquipmentTypeVOList = Lists.newArrayList();
    private Set<String> equipmetTypeNameSet = Sets.newHashSet();
    private String uuid;

    public ImportEquipmentTypeListener() {
        initJudgeMap();
    }


    public String getUuid() {
        return uuid;
    }

    @Override
    public void invoke(ImportEquipmentTypeVO importEquipmentTypeVO,
        AnalysisContext analysisContext) {
        importEquipmentTypeVO.setErrorMsg("");
        String errorMsg = "";
        if (StringUtils.isBlank(importEquipmentTypeVO.getEquipmentTypeName())) {
            errorMsg += "设备类型名称不能为空,";
        }
        if (StringUtils.isBlank(errorMsg)) {
            if (equipmetTypeNameSet.contains(importEquipmentTypeVO.getEquipmentTypeName())) {
                errorMsg += "设备类型已存在,";
            } else {
                equipmetTypeNameSet.add(importEquipmentTypeVO.getEquipmentTypeName());
            }
        }
        if (StringUtils.isNotBlank(errorMsg)) {
            uuid = UUID.randomUUID().toString();
            importEquipmentTypeVO.setErrorMsg(errorMsg.substring(0, errorMsg.length() - 1));
        }
        importEquipmentTypeVOList.add(importEquipmentTypeVO);
        if (StringUtils.isBlank(uuid)) {
            LockEquipmentType equipmentType = new LockEquipmentType();
            equipmentType.setName(importEquipmentTypeVO.getEquipmentTypeName());
            equipmentType.setDescription(importEquipmentTypeVO.getEquipmentTypeDescription());
            CommonUtils.addCommonParams(equipmentType);
            cachedDataList.add(equipmentType);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if (StringUtils.isNotBlank(uuid)) {
            ImportErrorCache.errorMap.put(uuid, importEquipmentTypeVOList);
            return;
        }
        SpringUtils.getBean(ILockEquipmentTypeService.class).saveBatch(cachedDataList);
    }

    private void initJudgeMap() {
        List<LockEquipmentType> equipmentTypeList = SpringUtils.getBean(
            ILockEquipmentTypeService.class).selectEquipmentTypeList(new LockCommonParamVO());
        for (LockEquipmentType Type : equipmentTypeList) {
            equipmetTypeNameSet.add(Type.getName());
        }
    }
}
