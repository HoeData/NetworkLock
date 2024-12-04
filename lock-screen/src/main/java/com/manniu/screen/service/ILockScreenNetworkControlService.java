package com.manniu.screen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manniu.screen.domain.LockScreenNetworkControl;
import com.manniu.screen.vo.param.LockScreenNetworkControlPageParamVO;
import java.util.List;

public interface ILockScreenNetworkControlService extends IService<LockScreenNetworkControl> {

    List<LockScreenNetworkControl> getNetworkControlList(LockScreenNetworkControlPageParamVO pageParamVO);
}
