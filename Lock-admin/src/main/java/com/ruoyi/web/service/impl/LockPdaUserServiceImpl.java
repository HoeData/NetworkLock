package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.web.domain.LockPdaInfo;
import com.ruoyi.web.domain.LockPdaUser;
import com.ruoyi.web.domain.vo.pda.LockPdaUserPageParamVO;
import com.ruoyi.web.mapper.LockPdaInfoMapper;
import com.ruoyi.web.mapper.LockPdaUserMapper;
import com.ruoyi.web.service.ILockPdaUserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LockPdaUserServiceImpl extends ServiceImpl<LockPdaUserMapper, LockPdaUser> implements
    ILockPdaUserService {

    private final LockPdaUserMapper pdaUserMapper;
    private final LockPdaInfoMapper pdaInfoMapper;

    @Override
    public List<LockPdaUser> getPdaUserList(LockPdaUserPageParamVO vo) {
        return pdaUserMapper.selectPdaUserList(vo);
    }

    @Override
    public void judgeUserName(LockPdaUser pdaUser) {
        LambdaQueryWrapper<LockPdaUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LockPdaUser::getUserName, pdaUser.getUserName());
        wrapper.eq(LockPdaUser::getDelFlag, 0);
        wrapper.eq(LockPdaUser::getPdaId, pdaUser.getPdaId());
        LockPdaUser old = getOne(wrapper);
        if (null != old && null == pdaUser.getId()) {
            throw new ServiceException("用户名已存在");
        }
        if (null != old && !old.getId().equals(pdaUser.getId())) {
            throw new ServiceException("用户名已存在");
        }
    }

    @Override
    public void judgeAdminFlag(LockPdaUser pdaUser) {
        if (null != pdaUser.getAdminFlag() && pdaUser.getAdminFlag() == 0) {
            throw new ServiceException("默认用户不允许修改");
        }
    }

    @Override
    public int deleteByIds(String[] ids) {
        for (String id : ids) {
            LockPdaUser lockPdaUser = getById(id);
            judgeAdminFlag(lockPdaUser);
            judgeWhetherEdit(lockPdaUser.getPdaId());
        }
        return pdaUserMapper.deleteByIds(ids);
    }

    @Override
    public void judgeWhetherEdit(Integer pdaId) {
        LockPdaInfo pdaInfo = pdaInfoMapper.selectById(pdaId);
        if (null == pdaInfo) {
            throw new ServiceException("PDA/电子钥匙不存在");
        }
        if (pdaInfo.getType() == 1) {
            throw new ServiceException("电子钥匙无法修改默认用户");
        }
    }

    @Override
    public int delByPdaId(String[] ids) {
        return pdaUserMapper.delByPdaId(ids);
    }
}
