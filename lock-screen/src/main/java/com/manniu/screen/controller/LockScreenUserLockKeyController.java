package com.manniu.screen.controller;

import com.manniu.screen.domain.LockScreenUserLockKey;
import com.manniu.screen.service.ILockScreenUserLockKeyService;
import com.manniu.screen.utils.CommonUtils;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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


    @PostMapping("/saveOrUpdate")
    public AjaxResult saveOrUpdate(@RequestBody LockScreenUserLockKey lockScreenUserLockKey) {
        CommonUtils.addCommonParams(lockScreenUserLockKey, lockScreenUserLockKey.getId());
        return toAjax(userLockKeyService.saveOrUpdate(lockScreenUserLockKey));
    }

    @GetMapping("/{userId}")
    public AjaxResult remove(@PathVariable String userId) {
        return AjaxResult.success(userLockKeyService.getByUserId(Long.valueOf(userId)));
    }

}
