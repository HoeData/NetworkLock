package com.manniu.screen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manniu.screen.config.LockScreenCache;
import com.manniu.screen.config.ReceiveDeviceMessageImpl;
import com.manniu.screen.constans.CommonConst;
import com.manniu.screen.domain.LockScreenElectronicLock;
import com.manniu.screen.mapper.LockScreenElectronicLockMapper;
import com.manniu.screen.service.ILockScreenElectronicLockService;
import com.manniu.screen.vo.LockStatusVO;
import com.manniu.screen.vo.param.LockScreenElectronicLockPageParamVO;
import com.manniu.screen.vo.view.LockScreenElectronicLockViewVO;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
                    LockScreenCache.lockStatusVOMap.getOrDefault(item.getDeviceId(),
                            new HashMap<>()).getOrDefault(item.getLockId(), new LockStatusVO())
                        .getDoorStatus());
                item.setLockStatusStr(
                    LockScreenCache.lockStatusVOMap.getOrDefault(item.getDeviceId(),
                            new HashMap<>()).getOrDefault(item.getLockId(), new LockStatusVO())
                        .getLockStatus());
                item.setOnlineStatusStr(
                    LockScreenCache.lockStatusVOMap.getOrDefault(item.getDeviceId(),
                            new HashMap<>()).getOrDefault(item.getLockId(), new LockStatusVO())
                        .getOnlineStatus());
                setStatus(item);
            } catch (Exception e) {
                setStatus(item);
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

    private void setStatus(LockScreenElectronicLockViewVO item) {
        if (StringUtils.isBlank(item.getDoorStatusStr())) {
            item.setDoorStatusStr(ReceiveDeviceMessageImpl.HORIZONTAL_LINE);
        }
        if (StringUtils.isBlank(item.getLockStatusStr())) {
            item.setLockStatusStr(ReceiveDeviceMessageImpl.HORIZONTAL_LINE);
        }
        if (StringUtils.isBlank(item.getOnlineStatusStr())) {
            item.setOnlineStatusStr(ReceiveDeviceMessageImpl.HORIZONTAL_LINE);
        }
    }
}
