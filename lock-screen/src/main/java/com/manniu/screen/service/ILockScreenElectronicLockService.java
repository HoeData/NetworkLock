package com.manniu.screen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manniu.screen.domain.LockScreenElectronicLock;
import com.manniu.screen.vo.param.LockScreenElectronicLockPageParamVO;
import com.manniu.screen.vo.view.LockScreenElectronicLockViewVO;
import java.util.List;

public interface ILockScreenElectronicLockService extends IService<LockScreenElectronicLock> {

    List<LockScreenElectronicLockViewVO> getElectronicLockList(LockScreenElectronicLockPageParamVO pageParamVO);

    List<LockScreenElectronicLock> getByNetworkControlId(Integer networkControlId);
}
