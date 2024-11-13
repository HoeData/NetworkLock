package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.web.annotation.CompanyScope;
import com.ruoyi.web.constants.CommonConst;
import com.ruoyi.web.domain.CompanyTreeSelect;
import com.ruoyi.web.domain.LockCompany;
import com.ruoyi.web.domain.LockDept;
import com.ruoyi.web.domain.LockSite;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.mapper.LockCompanyMapper;
import com.ruoyi.web.service.ILockCompanyService;
import com.ruoyi.web.service.ILockDeptService;
import com.ruoyi.web.service.ILockSiteService;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LockCompanyServiceImpl extends ServiceImpl<LockCompanyMapper, LockCompany> implements
    ILockCompanyService {

    private final LockCompanyMapper lockCompanyMapper;

    private final RedisCache redisCache;

    private static final String COMPANY = "company:";
    private final ILockDeptService lockDeptService;
    private final ILockSiteService siteService;

    @Override
    @CompanyScope()
    public List<LockCompany> selectCompanyList(LockCommonParamVO lockCommonParamVO) {
        return lockCompanyMapper.selectCompanyList(lockCommonParamVO);
    }

    @Override
    public int deleteByIds(String[] ids) {
        judgeDelete(ids);
        return lockCompanyMapper.deleteByIds(ids);
    }

    private void judgeDelete(String[] ids) {
        for (String id : ids) {
            LambdaQueryWrapper<LockCompany> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(LockCompany::getParentId, id);
            lambdaQueryWrapper.eq(LockCompany::getDelFlag, 0);
            if (count(lambdaQueryWrapper) > 0) {
                throw new ServiceException("删除公司存在下属公司,无法删除");
            }
            LambdaQueryWrapper<LockDept> lockDeptLambdaQueryWrapper = new LambdaQueryWrapper<>();
            lockDeptLambdaQueryWrapper.eq(LockDept::getCompanyId, id);
            lockDeptLambdaQueryWrapper.eq(LockDept::getDelFlag, 0);
            if (lockDeptService.count(lockDeptLambdaQueryWrapper) > 0) {
                throw new ServiceException("删除公司存在下属部门,无法删除");
            }
            LambdaQueryWrapper<LockSite> siteLambdaQueryWrapper = new LambdaQueryWrapper<>();
            siteLambdaQueryWrapper.eq(LockSite::getCompanyId, id);
            siteLambdaQueryWrapper.eq(LockSite::getDelFlag, 0);
            if (siteService.count(siteLambdaQueryWrapper) > 0) {
                throw new ServiceException("删除公司存在下属站点,无法删除");
            }
        }
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
        if (null != old && !old.getId().equals(id)) {
            throw new ServiceException("公司名称已存在");
        }
    }

    @Override
    public int saveOrUpdateAll(LockCompany lockCompany) {
        String path = CommonConst.ZERO + CommonConst.POINT;
        if (null != lockCompany.getParentId() && lockCompany.getParentId() != CommonConst.ZERO) {
            path = getById(lockCompany.getParentId()).getPath();
        }
        if (null == lockCompany.getId()) {
            save(lockCompany);
        }
        lockCompany.setPath(path + lockCompany.getId() + CommonConst.POINT);
        return updateById(lockCompany) ? 1 : 0;
    }

    @Override
    public List<CompanyTreeSelect> buildTreeSelect(List<LockCompany> list) {
        return buildTree(list).stream().map(CompanyTreeSelect::new).collect(Collectors.toList());
    }

    @Override
    public LockCompany getByIdCache(Integer companyId) {
        if (redisCache.hasKey(COMPANY + companyId)) {
            return redisCache.getCacheObject(COMPANY + companyId);
        }
        LockCompany lockCompany = getById(companyId);
        redisCache.expire(COMPANY + companyId, 60 * 10);
        return lockCompany;
    }

    private List<LockCompany> buildTree(List<LockCompany> list) {
        List<LockCompany> returnList = Lists.newArrayList();
        List<Integer> tempList = list.stream().map(LockCompany::getId).collect(Collectors.toList());
        for (LockCompany company : list) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(company.getParentId())) {
                recursionFn(list, company);
                returnList.add(company);
            }
        }
        if (returnList.isEmpty()) {
            returnList = list;
        }
        return returnList;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<LockCompany> list, LockCompany t) {
        // 得到子节点列表
        List<LockCompany> childList = getChildList(list, t);
        t.setChildren(childList);
        for (LockCompany tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<LockCompany> getChildList(List<LockCompany> list, LockCompany t) {
        List<LockCompany> tlist = Lists.newArrayList();
        Iterator<LockCompany> it = list.iterator();
        while (it.hasNext()) {
            LockCompany n = it.next();
            if (StringUtils.isNotNull(n.getParentId())
                && n.getParentId() == t.getId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<LockCompany> list, LockCompany t) {
        return getChildList(list, t).size() > 0;
    }
}
