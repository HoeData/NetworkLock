package com.ruoyi.web.controller.lock;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.web.service.LockIndexService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lock/index")
@RequiredArgsConstructor
public class LockIndexController extends BaseController {

    private final LockIndexService lockIndexService;
    @GetMapping("/getIndexStatisticalQuantity")
    public AjaxResult getIndexStatisticalQuantity() {
        return success(lockIndexService.getIndexStatisticalQuantity());
    }
}