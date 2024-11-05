package com.ruoyi.web.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.web.domain.LockCompany;
import com.ruoyi.web.domain.LockSite;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.domain.vo.importvo.ImportSiteVO;
import com.ruoyi.web.service.ILockCompanyService;
import com.ruoyi.web.service.ILockSiteService;
import com.ruoyi.web.utils.CommonUtils;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;


public class ImportSiteListener implements ReadListener<ImportSiteVO> {

    private List<LockSite> cachedDataList = Lists.newArrayList();
    private List<ImportSiteVO> importSiteVOList = Lists.newArrayList();
    private Map<String, Map<String, Integer>> judgeMap = Maps.newHashMap();
    private Set<String> siteNameSet = Sets.newHashSet();
    private String uuid;

    public ImportSiteListener() {
        initJudgeMap();
    }


    public String getUuid() {
        return uuid;
    }

    @Override
    public void invoke(ImportSiteVO importSiteVO, AnalysisContext analysisContext) {
        importSiteVO.setErrorMsg("");
        String errorMsg = "";
        if (StringUtils.isBlank(importSiteVO.getSiteName())) {
            errorMsg += "站点名称不能为空,";
        }
        if (StringUtils.isBlank(importSiteVO.getCompanyName())) {
            errorMsg += "所属公司不能为空,";
        }
        if (StringUtils.isBlank(errorMsg)) {
            if (!judgeMap.get("companyMap").containsKey(importSiteVO.getCompanyName())) {
                errorMsg += "所属公司错误,";
            } else {
                importSiteVO.setCompanyId(
                    judgeMap.get("companyMap").get(importSiteVO.getCompanyName()));
                if (siteNameSet.contains(
                    importSiteVO.getSiteName() + ";" + importSiteVO.getCompanyId())) {
                    errorMsg += "站点已存在,";
                } else {
                    siteNameSet.add(importSiteVO.getSiteName() + ";" + importSiteVO.getCompanyId());
                }
            }
        }
        if (StringUtils.isNotBlank(errorMsg)) {
            uuid = UUID.randomUUID().toString();
            importSiteVO.setErrorMsg(errorMsg.substring(0, errorMsg.length() - 1));
        }
        importSiteVOList.add(importSiteVO);
        if (StringUtils.isBlank(uuid)) {
            LockSite lockSite = new LockSite();
            lockSite.setName(importSiteVO.getSiteName());
            lockSite.setDescription(importSiteVO.getSiteDescription());
            lockSite.setCompanyId(importSiteVO.getCompanyId());
            lockSite.setLongitude(importSiteVO.getLongitude());
            lockSite.setLatitude(importSiteVO.getLatitude());
            CommonUtils.addCommonParams(lockSite);
            cachedDataList.add(lockSite);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if (StringUtils.isNotBlank(uuid)) {
            ImportErrorCache.errorMap.put(uuid, importSiteVOList);
            return;
        }
        SpringUtils.getBean(ILockSiteService.class).saveBatch(cachedDataList);
    }

    private void initJudgeMap() {
        List<LockCompany> companyList = SpringUtils.getBean(ILockCompanyService.class)
            .selectCompanyList(new LockCommonParamVO());
        Map<String, Integer> companyMap = companyList.stream()
            .collect(Collectors.toMap(LockCompany::getName, LockCompany::getId, (a, b) -> b));
        judgeMap.put("companyMap", companyMap);
        List<LockSite> siteList = SpringUtils.getBean(ILockSiteService.class)
            .getAll(new LockCommonParamVO());
        for (LockSite lockSite : siteList) {
            siteNameSet.add(lockSite.getName() + ";" + lockSite.getCompanyId());
        }
    }
}
