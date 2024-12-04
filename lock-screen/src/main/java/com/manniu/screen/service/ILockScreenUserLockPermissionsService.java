package com.manniu.screen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manniu.screen.domain.LockScreenUserLockPermissions;
import com.manniu.screen.vo.param.LockScreenUserLockPermissionsPageParamVO;
import com.manniu.screen.vo.view.LockScreenUserLockPermissionsViewVO;
import java.util.List;

public interface ILockScreenUserLockPermissionsService extends
    IService<LockScreenUserLockPermissions> {

    boolean mySaveOrUpdate(LockScreenUserLockPermissions lockPermissions);

    List<LockScreenUserLockPermissionsViewVO> getUserLockPermissionsList(
        LockScreenUserLockPermissionsPageParamVO pageParamVO);
}
