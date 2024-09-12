package com.ruoyi.web.controller.pda;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.web.domain.LockPdaInfo;
import com.ruoyi.web.enums.PdaDataSynchronizationStatusType;
import com.ruoyi.web.service.ILockPdaInfoService;
import com.ruoyi.web.utils.PdaDataSynchronizationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pda/dataSynchronization")
@RequiredArgsConstructor
@Slf4j
@Anonymous
public class PdaDataSynchronizationController {

    private final ILockPdaInfoService pdaInfoService;

    @GetMapping("/start")
    public AjaxResult start() {
        String deviceId = PdaDataSynchronizationUtil.getConnectedDeviceId();
        if (StringUtils.isBlank(PdaDataSynchronizationUtil.getConnectedDeviceId())) {
            return AjaxResult.error("未连接设备无法同步数据");
        }
        LockPdaInfo pdaInfo = pdaInfoService.getByKey(deviceId);
        if (null == pdaInfo) {
            return AjaxResult.error(PdaDataSynchronizationStatusType.NO_MATCH.getMsg());
        }
        if (PdaDataSynchronizationUtil.RUNNING) {
            return AjaxResult.error("正在同步数据无法同步,请等待数据同步完成后再进行同步");
        }
        PdaDataSynchronizationUtil.refreshAll();
        PdaDataSynchronizationUtil.startPdaDataSynchronizationThread();
        return AjaxResult.success("正在同步");
    }

    @GetMapping("/getStatus")
    public AjaxResult getStatus() {
        return AjaxResult.success(PdaDataSynchronizationUtil.nowStatusMsg);
    }
}
