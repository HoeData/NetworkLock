package com.manniu.screen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manniu.screen.domain.LockScreenUserLockPermissions;
import com.manniu.screen.vo.param.LockScreenUserLockPermissionsPageParamVO;
import com.manniu.screen.vo.view.LockScreenUserLockPermissionsViewVO;
import java.util.List;

public interface LockScreenUserLockPermissionsMapper extends
    BaseMapper<LockScreenUserLockPermissions> {

    List<LockScreenUserLockPermissionsViewVO> selectUserLockPermissionsList(
        LockScreenUserLockPermissionsPageParamVO pageParamVO);
}
