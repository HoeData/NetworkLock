package com.ruoyi.web.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.web.domain.LockEquipmentModel;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.domain.vo.importvo.ImportEquipmentModelVO;
import com.ruoyi.web.service.ILockEquipmentModelService;
import com.ruoyi.web.utils.CommonUtils;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;


public class ImportEquipmentModelListener implements ReadListener<ImportEquipmentModelVO> {

    private List<LockEquipmentModel> cachedDataList = Lists.newArrayList();
    private List<ImportEquipmentModelVO> importEquipmentModelVOList = Lists.newArrayList();
    private Set<String> equipmetModelNameSet = Sets.newHashSet();
    private String uuid;

    public ImportEquipmentModelListener() {
        initJudgeMap();
    }


    public String getUuid() {
        return uuid;
    }

    @Override
    public void invoke(ImportEquipmentModelVO importEquipmentModelVO,
        AnalysisContext analysisContext) {
        importEquipmentModelVO.setErrorMsg("");
        String errorMsg = "";
        if (StringUtils.isBlank(importEquipmentModelVO.getEquipmentModelName())) {
            errorMsg += "设备型号名称不能为空,";
        }
        if (StringUtils.isBlank(errorMsg)) {
            if (equipmetModelNameSet.contains(importEquipmentModelVO.getEquipmentModelName())) {
                errorMsg += "设备型号已存在,";
            } else {
                equipmetModelNameSet.add(importEquipmentModelVO.getEquipmentModelName());
            }
        }
        if (StringUtils.isNotBlank(errorMsg)) {
            uuid = UUID.randomUUID().toString();
            importEquipmentModelVO.setErrorMsg(errorMsg.substring(0, errorMsg.length() - 1));
        }
        importEquipmentModelVOList.add(importEquipmentModelVO);
        if (StringUtils.isBlank(uuid)) {
            LockEquipmentModel equipmentModel = new LockEquipmentModel();
            equipmentModel.setName(importEquipmentModelVO.getEquipmentModelName());
            equipmentModel.setDescription(importEquipmentModelVO.getEquipmentModelDescription());
            CommonUtils.addCommonParams(equipmentModel);
            cachedDataList.add(equipmentModel);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if (StringUtils.isNotBlank(uuid)) {
            ImportErrorCache.errorMap.put(uuid, importEquipmentModelVOList);
            return;
        }
        SpringUtils.getBean(ILockEquipmentModelService.class).saveBatch(cachedDataList);
    }

    private void initJudgeMap() {
        List<LockEquipmentModel> equipmentModelList = SpringUtils.getBean(
            ILockEquipmentModelService.class).selectEquipmentModelList(new LockCommonParamVO());
        for (LockEquipmentModel model : equipmentModelList) {
            equipmetModelNameSet.add(model.getName());
        }
    }
}
