package com.manniu.screen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manniu.screen.config.LockScreenCache;
import com.manniu.screen.constans.CommonConst;
import com.manniu.screen.domain.LockScreenElectronicLock;
import com.manniu.screen.mapper.LockScreenElectronicLockMapper;
import com.manniu.screen.service.ILockScreenElectronicLockService;
import com.manniu.screen.vo.param.LockScreenElectronicLockPageParamVO;
import com.manniu.screen.vo.view.LockScreenElectronicLockViewVO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LockScreenElectronicLockServiceImpl extends
    ServiceImpl<LockScreenElectronicLockMapper, LockScreenElectronicLock> implements
    ILockScreenElectronicLockService {

    private final LockScreenElectronicLockMapper lockScreenElectronicLockMapper;

    @Override
    public List<LockScreenElectronicLockViewVO> getElectronicLockList(
        LockScreenElectronicLockPageParamVO pageParamVO) {
        List<LockScreenElectronicLockViewVO> list = lockScreenElectronicLockMapper.selectElectronicLockList(
            pageParamVO);
        list.forEach(item -> {
            try {
                item.setDoorStatusStr(
                    LockScreenCache.lockStatusVOMap.get(item.getDeviceId()).get(item.getLockId())
                        .getDoorStatus());
                item.setLockStatusStr(
                    LockScreenCache.lockStatusVOMap.get(item.getDeviceId()).get(item.getLockId())
                        .getLockStatus());
                item.setOnlineStatusStr(
                    LockScreenCache.lockStatusVOMap.get(item.getDeviceId()).get(item.getLockId())
                        .getOnlineStatus());
            } catch (Exception e) {
            }
        });
        return list;
    }

    @Override
    public List<LockScreenElectronicLock> getByNetworkControlId(Integer networkControlId) {
        LambdaQueryWrapper<LockScreenElectronicLock> electronicLockLambdaQueryWrapper = new LambdaQueryWrapper<>();
        electronicLockLambdaQueryWrapper.eq(LockScreenElectronicLock::getNetworkControlId,
            networkControlId);
        electronicLockLambdaQueryWrapper.eq(LockScreenElectronicLock::getDelFlag,
            CommonConst.ZERO_STR);
        return list(electronicLockLambdaQueryWrapper);
    }
}
