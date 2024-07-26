package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
    public List<LockCommonViewVO> selectSiteList(LockCommonParamVO lockCommonParamVO) {
        return lockSiteMapper.selectSiteList(lockCommonParamVO);
    }

    @Override
    public int deleteByIds(String[] ids) {
        return lockSiteMapper.deleteByIds(ids);
    }
}
