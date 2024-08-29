package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.ServiceException;
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

    @Override
    public void judgeName(String name, Integer id) {
        LambdaQueryWrapper<LockCompany> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LockCompany::getName, name);
        wrapper.eq(LockCompany::getDelFlag, 0);
        LockCompany old = getOne(wrapper);
        if (null != old && null == id) {
            throw new ServiceException("公司名称已存在");
        }
        if (null != old) {
            if (!old.getId().equals(id)) {
                throw new ServiceException("公司名称已存在");
            }
        }
    }
}
