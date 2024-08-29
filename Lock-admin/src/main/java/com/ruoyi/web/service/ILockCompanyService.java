package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.web.domain.LockCompany;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import java.util.List;

public interface ILockCompanyService extends IService<LockCompany> {


    List<LockCompany> selectCompanyList(LockCommonParamVO lockCommonParamVO);

    int deleteByIds(String[] ids);

    void judgeName(String name, Integer id);
}
