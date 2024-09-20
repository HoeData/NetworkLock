package com.ruoyi.web.controller.lock;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.web.constants.LockCache;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lock/baseInfo")
public class LockBaseInfoController extends BaseController {


    @GetMapping("/list")
    public AjaxResult list() {
        return success(LockCache.licenseParamVOList);
    }
    @GetMapping("/getAllLockSerialNumberList")
    public AjaxResult getAllLockSerialNumberList() {
        return success(LockCache.lockSerialNumberSet);
    }

}
