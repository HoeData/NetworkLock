package com.ruoyi.web.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.web.domain.LockCompany;
import com.ruoyi.web.domain.LockDept;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.domain.vo.importvo.ImportDeptVO;
import com.ruoyi.web.service.ILockCompanyService;
import com.ruoyi.web.service.ILockDeptService;
import com.ruoyi.web.utils.CommonUtils;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;


public class ImportDeptListener implements ReadListener<ImportDeptVO> {

    private List<LockDept> cachedDataList = Lists.newArrayList();
    private List<ImportDeptVO> importDeptVOList = Lists.newArrayList();
    private Map<String, Map<String, Integer>> judgeMap = Maps.newHashMap();
    private Set<String> deptNameSet = Sets.newHashSet();
    private String uuid;

    public ImportDeptListener() {
        initJudgeMap();
    }


    public String getUuid() {
        return uuid;
    }

    @Override
    public void invoke(ImportDeptVO importDeptVO, AnalysisContext analysisContext) {
        importDeptVO.setErrorMsg("");
        String errorMsg = "";
        if (StringUtils.isBlank(importDeptVO.getDeptName())) {
            errorMsg += "部门名称不能为空,";
        }
        if (StringUtils.isBlank(importDeptVO.getCompanyName())) {
            errorMsg += "所属公司不能为空,";
        }
        if (StringUtils.isBlank(errorMsg)) {
            if (!judgeMap.get("companyMap").containsKey(importDeptVO.getCompanyName())) {
                errorMsg += "所属公司错误,";
            } else {
                importDeptVO.setCompanyId(
                    judgeMap.get("companyMap").get(importDeptVO.getCompanyName()));
                if (deptNameSet.contains(
                    importDeptVO.getDeptName() + ";" + importDeptVO.getCompanyId())) {
                    errorMsg += "部门已存在,";
                } else {
                    deptNameSet.add(importDeptVO.getDeptName() + ";" + importDeptVO.getCompanyId());
                }
            }
        }
        if (StringUtils.isNotBlank(errorMsg)) {
            uuid = UUID.randomUUID().toString();
            importDeptVO.setErrorMsg(errorMsg.substring(0, errorMsg.length() - 1));
        }
        importDeptVOList.add(importDeptVO);
        if (StringUtils.isBlank(uuid)) {
            LockDept lockDept = new LockDept();
            lockDept.setName(importDeptVO.getDeptName());
            lockDept.setDescription(importDeptVO.getDeptDescription());
            lockDept.setCompanyId(importDeptVO.getCompanyId());
            CommonUtils.addCommonParams(lockDept);
            cachedDataList.add(lockDept);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if (StringUtils.isNotBlank(uuid)) {
            ImportErrorCache.errorMap.put(uuid, importDeptVOList);
            return;
        }
        SpringUtils.getBean(ILockDeptService.class).saveBatch(cachedDataList);
    }

    private void initJudgeMap() {
        List<LockCompany> companyList = SpringUtils.getBean(ILockCompanyService.class)
            .selectCompanyList(new LockCommonParamVO());
        Map<String, Integer> companyMap = companyList.stream()
            .collect(Collectors.toMap(LockCompany::getName, LockCompany::getId, (a, b) -> b));
        judgeMap.put("companyMap", companyMap);
        List<LockDept> deptList = SpringUtils.getBean(ILockDeptService.class)
            .getAll(new LockCommonParamVO());
        for (LockDept lockDept : deptList) {
            deptNameSet.add(lockDept.getName() + ";" + lockDept.getCompanyId());
        }
    }
}
