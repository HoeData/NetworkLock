package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.web.annotation.CompanyScope;
import com.ruoyi.web.domain.LockDept;
import com.ruoyi.web.domain.LockSite;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.domain.vo.LockCommonViewVO;
import com.ruoyi.web.mapper.LockSiteMapper;
import com.ruoyi.web.service.ILockSiteService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class LockSiteServiceImpl extends
    ServiceImpl<LockSiteMapper, LockSite> implements ILockSiteService {

    @Resource
    private LockSiteMapper lockSiteMapper;


    @Override
    @CompanyScope()
    public List<LockCommonViewVO> selectSiteList(LockCommonParamVO lockCommonParamVO) {
        return lockSiteMapper.selectSiteList(lockCommonParamVO);
    }

    @Override
    public int deleteByIds(String[] ids) {
        return lockSiteMapper.deleteByIds(ids);
    }

    @Override
    public void judgeName(LockSite lockSite) {
        LambdaQueryWrapper<LockSite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LockSite::getName, lockSite.getName());
        wrapper.eq(LockSite::getDelFlag, 0);
        wrapper.eq(LockSite::getCompanyId, lockSite.getCompanyId());
        LockSite old = getOne(wrapper);
        if (null != old && null == lockSite.getId()) {
            throw new ServiceException("站点名称已存在");
        }
        if (null != old) {
            if (!old.getId().equals(lockSite.getId())) {
                throw new ServiceException("站点名称已存在");
            }
        }
    }

    @Override
    @CompanyScope()
    public List<LockSite> getAll(LockCommonParamVO lockCommonParamVO) {
        return lockSiteMapper.selectAllList(lockCommonParamVO);
    }
}
