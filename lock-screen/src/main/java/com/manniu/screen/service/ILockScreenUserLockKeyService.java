package com.manniu.screen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manniu.screen.domain.LockScreenUserLockKey;

public interface ILockScreenUserLockKeyService extends IService<LockScreenUserLockKey> {

    public LockScreenUserLockKey getByUserId(Long userId);
}
