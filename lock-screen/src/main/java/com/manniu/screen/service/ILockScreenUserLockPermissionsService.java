package com.manniu.screen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manniu.screen.domain.LockScreenUserLockPermissions;
import com.manniu.screen.vo.CommonVO;
import com.manniu.screen.vo.param.LockScreenUserLockPermissionsPageParamVO;
import com.manniu.screen.vo.view.LockScreenUserLockPermissionsViewVO;
import com.ruoyi.common.core.domain.AjaxResult;
import java.util.List;
import java.util.Map;

public interface ILockScreenUserLockPermissionsService extends
    IService<LockScreenUserLockPermissions> {
    Map<String, Object> mySaveOrUpdate(List<LockScreenUserLockPermissions> lockPermissionsList);

    List<LockScreenUserLockPermissionsViewVO> getUserLockPermissionsList(
        LockScreenUserLockPermissionsPageParamVO pageParamVO);

    int removeByUserId(CommonVO commonVO);

    AjaxResult updateByUserId(Integer userId);
}
