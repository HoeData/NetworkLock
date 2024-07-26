package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
}
