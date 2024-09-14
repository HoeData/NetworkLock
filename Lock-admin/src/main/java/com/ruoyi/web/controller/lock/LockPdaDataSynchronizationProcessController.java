package com.ruoyi.web.controller.lock;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.web.service.ILockPdaDataSynchronizationProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lock/pdaDataSynchronizationProcess")
@RequiredArgsConstructor
public class LockPdaDataSynchronizationProcessController {

    private final ILockPdaDataSynchronizationProcessService pdaDataSynchronizationProcessService;

    @PostMapping("/listBySynchronizationId/{synchronizationId}")
    public AjaxResult list(@PathVariable Integer synchronizationId) {
        return AjaxResult.success(
            pdaDataSynchronizationProcessService.selectBySynchronizationId(synchronizationId));
    }
}
