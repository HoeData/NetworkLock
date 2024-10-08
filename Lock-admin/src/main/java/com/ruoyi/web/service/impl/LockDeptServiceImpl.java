package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.web.domain.LockCompany;
import com.ruoyi.web.domain.LockDept;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.domain.vo.LockCommonViewVO;
import com.ruoyi.web.mapper.LockDeptMapper;
import com.ruoyi.web.service.ILockDeptService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class LockDeptServiceImpl extends ServiceImpl<LockDeptMapper, LockDept> implements
    ILockDeptService {
    @Resource
    private LockDeptMapper lockDeptMapper;


    @Override
    public List<LockCommonViewVO> selectDeptList(LockCommonParamVO lockCommonParamVO) {
        return lockDeptMapper.selectDeptList(lockCommonParamVO);
    }

    @Override
    public int deleteByIds(String[] ids) {
        return lockDeptMapper.deleteByIds(ids);
    }

    @Override
    public void judgeName(LockDept lockDept) {
        LambdaQueryWrapper<LockDept> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LockDept::getName, lockDept.getName());
        wrapper.eq(LockDept::getDelFlag, 0);
        wrapper.eq(LockDept::getCompanyId, lockDept.getCompanyId());
        LockDept old = getOne(wrapper);
        if (null != old && null == lockDept.getId()) {
            throw new ServiceException("部门名称已存在");
        }
        if (null != old) {
            if (!old.getId().equals(lockDept.getId())) {
                throw new ServiceException("部门名称已存在");
            }
        }
    }

    @Override
    public List<LockDept> getAll() {
        LambdaQueryWrapper<LockDept> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(LockDept::getDelFlag,0);
        return list(wrapper);
    }
}
