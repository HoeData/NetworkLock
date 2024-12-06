package com.manniu.screen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manniu.screen.domain.LockScreenNetworkControl;
import com.manniu.screen.vo.param.LockScreenNetworkControlPageParamVO;
import java.util.List;
import java.util.Map;

public interface ILockScreenNetworkControlService extends IService<LockScreenNetworkControl> {

    List<LockScreenNetworkControl> getNetworkControlList(
        LockScreenNetworkControlPageParamVO pageParamVO);

    void saveAndUpdateControlAndLockByMap(Map<String, List<String>> map);
}
