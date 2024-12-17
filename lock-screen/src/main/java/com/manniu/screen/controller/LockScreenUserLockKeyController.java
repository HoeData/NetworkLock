package com.manniu.screen.controller;

import com.github.pagehelper.PageHelper;
import com.manniu.screen.domain.LockScreenUserLockKey;
import com.manniu.screen.service.ILockScreenUserLockKeyService;
import com.manniu.screen.service.ILockScreenUserLockPermissionsService;
import com.manniu.screen.vo.param.LockUserKeyPageParamVO;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lockScreenUserLockKey")
@RequiredArgsConstructor
public class LockScreenUserLockKeyController extends BaseController {

    private final ILockScreenUserLockKeyService userLockKeyService;
    private final ILockScreenUserLockPermissionsService lockPermissionsService;

    @PostMapping("/saveOrUpdate")
    public Object saveOrUpdate(@RequestBody LockScreenUserLockKey lockScreenUserLockKey) {
        Integer type = lockScreenUserLockKey.getType();
        if (!userLockKeyService.mySaveOrUpdate(lockScreenUserLockKey)) {
            return AjaxResult.error();
        }
        if (null != type) {
            return lockPermissionsService.updateByUserId(lockScreenUserLockKey.getId());
        }
        return AjaxResult.success();
    }

    @DeleteMapping("/{userId}")
    public AjaxResult remove(@PathVariable Integer userId) {
        return userLockKeyService.deleteByUserId(userId);
    }

    @PostMapping("/list")
    public TableDataInfo list(@RequestBody LockUserKeyPageParamVO pageParamVO) {
        PageHelper.startPage(pageParamVO.getPageNum(), pageParamVO.getPageSize());
        return getDataTable(userLockKeyService.getUserLockKeyList(pageParamVO));
    }
}
