package com.manniu.screen.controller;

import com.jinfu.lock.core.LockTemplate;
import com.jinfu.lock.pojo.LockResult;
import com.manniu.screen.service.ITestService;
import com.manniu.screen.vo.LockScreenApiParamVO;
import com.manniu.screen.domain.Test;
import com.ruoyi.common.core.controller.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lockScreen")
public class ScreenApiController extends BaseController {

    private final ITestService testService;
    private final LockTemplate lockTemplate;

    @RequestMapping("/aa")
    public String test() {
        Test test=new Test();
        test.setName("fafa");
        testService.save(test);
        return "aa";
    }

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
}
