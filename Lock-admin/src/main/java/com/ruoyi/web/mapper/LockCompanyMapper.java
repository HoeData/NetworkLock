package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.domain.LockCompany;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import java.util.List;

public interface LockCompanyMapper extends BaseMapper<LockCompany> {

    List<LockCompany> selectCompanyList(LockCommonParamVO lockCommonParamVO);

    int deleteByIds(String[] ids);
}
