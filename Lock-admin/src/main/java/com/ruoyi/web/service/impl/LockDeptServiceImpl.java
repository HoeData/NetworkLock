package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.web.annotation.CompanyScope;
import com.ruoyi.web.domain.LockDept;
import com.ruoyi.web.domain.LockEquipment;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.domain.vo.LockCommonViewVO;
import com.ruoyi.web.mapper.LockDeptMapper;
import com.ruoyi.web.service.ILockDeptService;
import com.ruoyi.web.service.ILockEquipmentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LockDeptServiceImpl extends ServiceImpl<LockDeptMapper, LockDept> implements
    ILockDeptService {

    private final LockDeptMapper lockDeptMapper;
    private final ILockEquipmentService lockEquipmentService;

    @Override
    @CompanyScope()
    public List<LockCommonViewVO> selectDeptList(LockCommonParamVO lockCommonParamVO) {
        return lockDeptMapper.selectDeptList(lockCommonParamVO);
    }

    @Override
    public int deleteByIds(String[] ids) {
        judgeDelete(ids);
        return lockDeptMapper.deleteByIds(ids);
    }

    private void judgeDelete(String[] ids) {
        for (String id : ids) {
            LambdaQueryWrapper<LockEquipment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(LockEquipment::getDeptId, id);
            lambdaQueryWrapper.eq(LockEquipment::getDelFlag, 0);
            if (lockEquipmentService.count(lambdaQueryWrapper) > 0) {
                throw new ServiceException("删除部门存在下属设备,无法删除");
            }
        }
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
        if (null != old && !old.getId().equals(lockDept.getId())) {
            throw new ServiceException("部门名称已存在");
        }
    }

    @Override
    @CompanyScope()
    public List<LockDept> getAll(LockCommonParamVO lockCommonParamVO) {
        return lockDeptMapper.selectAllList(lockCommonParamVO);
    }
}
