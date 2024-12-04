package com.manniu.screen.controller;

import com.github.pagehelper.PageHelper;
import com.manniu.screen.domain.LockScreenUserLockPermissions;
import com.manniu.screen.service.ILockScreenUserLockPermissionsService;
import com.manniu.screen.utils.CommonUtils;
import com.manniu.screen.vo.param.LockScreenUserLockPermissionsPageParamVO;
import com.manniu.screen.vo.view.LockScreenElectronicLockViewVO;
import com.manniu.screen.vo.view.LockScreenUserLockPermissionsViewVO;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lockPermissions")
@RequiredArgsConstructor
public class LockScreenUserLockPermissionsController extends BaseController {

    private final ILockScreenUserLockPermissionsService lockPermissionsService;

    @PostMapping("/saveOrUpdate")
    public AjaxResult saveOrUpdate(@RequestBody LockScreenUserLockPermissions lockPermissions) {
        CommonUtils.addCommonParams(lockPermissions, lockPermissions.getId());
        return toAjax(lockPermissionsService.mySaveOrUpdate(lockPermissions));
    }
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody LockScreenUserLockPermissionsPageParamVO pageParamVO) {
        PageHelper.startPage(pageParamVO.getPageNum(), pageParamVO.getPageSize());
        List<LockScreenUserLockPermissionsViewVO> list = lockPermissionsService.getUserLockPermissionsList(pageParamVO);
        return getDataTable(list);
    }
}
