package com.manniu.screen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manniu.screen.constans.CommonConst;
import com.manniu.screen.domain.LockScreenElectronicLock;
import com.manniu.screen.domain.LockScreenNetworkControl;
import com.manniu.screen.mapper.LockScreenNetworkControlMapper;
import com.manniu.screen.service.ILockScreenElectronicLockService;
import com.manniu.screen.service.ILockScreenNetworkControlService;
import com.manniu.screen.utils.CommonUtils;
import com.manniu.screen.vo.param.LockScreenNetworkControlPageParamVO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LockScreenNetworkControlServiceImpl extends
    ServiceImpl<LockScreenNetworkControlMapper, LockScreenNetworkControl> implements
    ILockScreenNetworkControlService {

    private final LockScreenNetworkControlMapper networkControlMapper;
    private final ILockScreenElectronicLockService electronicLockService;

    @Override
    public List<LockScreenNetworkControl> getNetworkControlList(
        LockScreenNetworkControlPageParamVO pageParamVO) {
        return networkControlMapper.selectNetworkControlList(pageParamVO);
    }

    public LockScreenNetworkControl getByIp(String ip) {
        LambdaQueryWrapper<LockScreenNetworkControl> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(LockScreenNetworkControl::getIp, ip);
        lambdaQueryWrapper.eq(LockScreenNetworkControl::getDelFlag, CommonConst.ZERO_STR);
        return getOne(lambdaQueryWrapper);
    }

    @Override
    @Transactional
    public void saveAndUpdateControlAndLockByMap(Map<String, List<String>> map) {
        map.forEach((k, lockIdlist) -> {
            String[] ipAndDeviceId = k.split(CommonConst.ENGLISH_COLON);
            LockScreenNetworkControl networkControl = getByIp(ipAndDeviceId[0]);
            networkControl.setIp(ipAndDeviceId[0]);
            networkControl.setDeviceId(ipAndDeviceId[1]);
            CommonUtils.addCommonParams(networkControl, networkControl.getId());
            saveOrUpdate(networkControl);
            List<LockScreenElectronicLock> electronicLockList = new ArrayList<>();
            lockIdlist.forEach(lockId -> {
                int lockInt = Integer.parseInt(lockId);
                List<LockScreenElectronicLock> screenElectronicLockList = electronicLockService.getByNetworkControlId(
                    networkControl.getId());
                Map<Integer, LockScreenElectronicLock> lockIdAndObjMap = screenElectronicLockList.stream()
                    .collect(Collectors.toMap(LockScreenElectronicLock::getLockId,
                        lockScreenElectronicLock -> lockScreenElectronicLock, (a, b) -> b));
                LockScreenElectronicLock electronicLock = new LockScreenElectronicLock();
                if (lockIdAndObjMap.containsKey(lockInt)) {
                    electronicLock = lockIdAndObjMap.get(lockInt);
                }
                electronicLock.setNetworkControlId(networkControl.getId());
                electronicLock.setLockId(lockInt);
                CommonUtils.addCommonParams(electronicLock, electronicLock.getId());
                electronicLockList.add(electronicLock);
            });
            electronicLockService.saveOrUpdateBatch(electronicLockList);
        });
    }
}
