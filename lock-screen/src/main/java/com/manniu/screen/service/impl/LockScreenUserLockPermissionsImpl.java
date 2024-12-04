package com.manniu.screen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manniu.screen.domain.LockScreenUserLockPermissions;
import com.manniu.screen.mapper.LockScreenUserLockPermissionsMapper;
import com.manniu.screen.service.ILockScreenUserLockPermissionsService;
import com.manniu.screen.vo.param.LockScreenUserLockPermissionsPageParamVO;
import com.manniu.screen.vo.view.LockScreenUserLockPermissionsViewVO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LockScreenUserLockPermissionsImpl extends
    ServiceImpl<LockScreenUserLockPermissionsMapper, LockScreenUserLockPermissions> implements
    ILockScreenUserLockPermissionsService {

    private final LockScreenUserLockPermissionsMapper lockScreenUserLockPermissionsMapper;

    @Override
    public boolean mySaveOrUpdate(LockScreenUserLockPermissions lockPermissions) {
        //TODO  需要调用API等设备来了在写 这个需要测试和ip通不通
        return saveOrUpdate(lockPermissions);

    }

    @Override
    public List<LockScreenUserLockPermissionsViewVO> getUserLockPermissionsList(
        LockScreenUserLockPermissionsPageParamVO pageParamVO) {
        return lockScreenUserLockPermissionsMapper.selectUserLockPermissionsList(pageParamVO);
    }
}
