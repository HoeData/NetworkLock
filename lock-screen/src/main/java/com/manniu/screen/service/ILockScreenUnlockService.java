package com.manniu.screen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manniu.screen.domain.LockScreenUnlock;
import com.manniu.screen.vo.param.LockScreenUnlockPageParamVO;
import com.manniu.screen.vo.view.LockScreenUnlockVIewVO;
import java.util.List;

public interface ILockScreenUnlockService extends IService<LockScreenUnlock> {

    List<LockScreenUnlockVIewVO> getUnlockList(LockScreenUnlockPageParamVO pageParamVO);
}
