package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.web.domain.CompanyTreeSelect;
import com.ruoyi.web.domain.LockCompany;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import java.util.List;

public interface ILockCompanyService extends IService<LockCompany> {


    List<LockCompany> selectCompanyList(LockCommonParamVO lockCommonParamVO);

    int deleteByIds(String[] ids);

    void judgeName(String name, Integer id);


    int saveOrUpdateAll(LockCompany lockCompany);

    List<CompanyTreeSelect> buildTreeSelect(List<LockCompany> list);
    LockCompany getByIdCache(Integer companyId);
}
