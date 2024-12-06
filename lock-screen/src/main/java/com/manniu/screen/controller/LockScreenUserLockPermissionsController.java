package com.manniu.screen.controller;

import com.github.pagehelper.PageHelper;
import com.manniu.screen.domain.LockScreenUserLockPermissions;
import com.manniu.screen.service.ILockScreenUserLockPermissionsService;
import com.manniu.screen.vo.CommonVO;
import com.manniu.screen.vo.param.LockScreenUserLockPermissionsPageParamVO;
import com.manniu.screen.vo.view.LockScreenUserLockPermissionsViewVO;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
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
    public Object saveOrUpdate(
        @RequestBody List<LockScreenUserLockPermissions> lockPermissionsList) {
        return lockPermissionsService.mySaveOrUpdate(lockPermissionsList);
    }

    @PostMapping("/list")
    public TableDataInfo list(
        @RequestBody @Validated LockScreenUserLockPermissionsPageParamVO pageParamVO) {
        PageHelper.startPage(pageParamVO.getPageNum(), pageParamVO.getPageSize());
        List<LockScreenUserLockPermissionsViewVO> list = lockPermissionsService.getUserLockPermissionsList(
            pageParamVO);
        return getDataTable(list);
    }

    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody CommonVO commonVO) {
        return toAjax(lockPermissionsService.removeByUserId(commonVO));
    }
}
