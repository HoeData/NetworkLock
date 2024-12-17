package com.manniu.screen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manniu.screen.domain.LockScreenUserLockKey;
import com.manniu.screen.vo.param.LockUserKeyPageParamVO;
import com.manniu.screen.vo.view.LockUserKeyViewVO;
import com.ruoyi.common.core.domain.AjaxResult;
import java.util.List;

public interface ILockScreenUserLockKeyService extends IService<LockScreenUserLockKey> {

     LockScreenUserLockKey getByUserId(Integer userId);

    boolean mySaveOrUpdate(LockScreenUserLockKey lockScreenUserLockKey);

    List<LockUserKeyViewVO> getUserLockKeyList(LockUserKeyPageParamVO pageParamVO);
    AjaxResult deleteByUserId(Integer userId);
}
