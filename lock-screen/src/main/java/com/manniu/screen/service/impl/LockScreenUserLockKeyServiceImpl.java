package com.manniu.screen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manniu.screen.constans.CommonConst;
import com.manniu.screen.domain.LockScreenUserLockKey;
import com.manniu.screen.mapper.LockScreenUserLockKeyMapper;
import com.manniu.screen.service.ILockScreenUserLockKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LockScreenUserLockKeyServiceImpl extends
    ServiceImpl<LockScreenUserLockKeyMapper, LockScreenUserLockKey> implements
    ILockScreenUserLockKeyService {

    @Override
    public LockScreenUserLockKey getByUserId(Long userId) {
        LambdaQueryWrapper<LockScreenUserLockKey> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LockScreenUserLockKey::getUserId, userId);
        queryWrapper.eq(LockScreenUserLockKey::getDelFlag, CommonConst.ZERO_STR);
        return getOne(queryWrapper);
    }
}
