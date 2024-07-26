package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.domain.LockCompany;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.mapper.LockCompanyMapper;
import com.ruoyi.web.service.ILockCompanyService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class LockCompanyServiceImpl extends
    ServiceImpl<LockCompanyMapper, LockCompany> implements ILockCompanyService {

    @Resource
    private LockCompanyMapper lockCompanyMapper;

    @Override
    public List<LockCompany> selectCompanyList(LockCommonParamVO lockCommonParamVO) {
        return lockCompanyMapper.selectCompanyList(lockCommonParamVO);
    }

    @Override
    public int deleteByIds(String[] ids) {
        return lockCompanyMapper.deleteByIds(ids);
    }
}
