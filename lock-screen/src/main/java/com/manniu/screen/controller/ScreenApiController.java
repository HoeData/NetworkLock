package com.manniu.screen.controller;

import com.jinfu.lock.core.LockTemplate;
import com.jinfu.lock.pojo.LockResult;
import com.manniu.screen.service.ILockScreenNetworkControlService;
import com.manniu.screen.vo.LockScreenApiParamVO;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lockScreen")
@Anonymous
public class ScreenApiController extends BaseController {

    private final LockTemplate lockTemplate;
    private final ILockScreenNetworkControlService networkControlService;


    /**
     * 获取设备心跳
     *
     * @param paramVO 参数vo
     * @return {@link LockResult }
     */
    @PostMapping("/getHeartBrat")
    public LockResult getHeartBeat(@RequestBody LockScreenApiParamVO paramVO) {
        return lockTemplate.heartBeat(paramVO.getIp(), paramVO.getDeviceId(), paramVO.getLockId());
    }

    /**
     * 远程解锁
     *
     * @param paramVO 参数vo
     * @return {@link LockResult }
     */
    @PostMapping("/remoteUnlock")
    public LockResult remoteUnlock(@RequestBody LockScreenApiParamVO paramVO) {
        return lockTemplate.openLock(paramVO.getIp(), paramVO.getDeviceId(), paramVO.getLockId());
    }

    /**
     * 设置全部在线设备
     *
     * @return {@link Object }
     */
    @GetMapping("/onlineAll")
    public AjaxResult onlineAll() {
        Map<String, List<String>> map = lockTemplate.onlineAll();
        networkControlService.saveAndUpdateControlAndLockByMap(map);
        return AjaxResult.success(map);
    }
    @GetMapping("/getsn")
    public AjaxResult getsn() {
       return AjaxResult.success(lockTemplate.getVerification("192.168.1.199","14080010",1));
    }
    @GetMapping("/setsn0")
    public AjaxResult setsn0() {
        return AjaxResult.success( lockTemplate.setVerification("192.168.1.199","14080010",1,"0"));
    }
    @GetMapping("/setsn1")
    public AjaxResult setsn1() {
        return AjaxResult.success( lockTemplate.setVerification("192.168.1.199","14080010",1,"1"));
    }
}
