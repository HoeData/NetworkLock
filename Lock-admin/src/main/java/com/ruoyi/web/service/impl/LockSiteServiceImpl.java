package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.web.annotation.CompanyScope;
import com.ruoyi.web.domain.LockMachineRoom;
import com.ruoyi.web.domain.LockSite;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.domain.vo.LockCommonViewVO;
import com.ruoyi.web.mapper.LockSiteMapper;
import com.ruoyi.web.service.ILockMachineRoomService;
import com.ruoyi.web.service.ILockSiteService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LockSiteServiceImpl extends ServiceImpl<LockSiteMapper, LockSite> implements
    ILockSiteService {


    private final LockSiteMapper lockSiteMapper;
    private final ILockMachineRoomService machineRoomService;


    @Override
    @CompanyScope()
    public List<LockCommonViewVO> selectSiteList(LockCommonParamVO lockCommonParamVO) {
        return lockSiteMapper.selectSiteList(lockCommonParamVO);
    }

    @Override
    public int deleteByIds(String[] ids) {
        judgeDelete(ids);
        return lockSiteMapper.deleteByIds(ids);
    }

    private void judgeDelete(String[] ids) {
        for (String id : ids) {
            LambdaQueryWrapper<LockMachineRoom> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(LockMachineRoom::getSiteId, id);
            lambdaQueryWrapper.eq(LockMachineRoom::getDelFlag, 0);
            if (machineRoomService.count(lambdaQueryWrapper) > 0) {
                throw new ServiceException("删除站点存在下属机房,无法删除");
            }
        }
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
