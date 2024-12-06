package com.manniu.screen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinfu.lock.core.LockTemplate;
import com.jinfu.lock.pojo.LockResult;
import com.manniu.screen.constans.CommonConst;
import com.manniu.screen.domain.LockScreenUserLockKey;
import com.manniu.screen.domain.LockScreenUserLockPermissions;
import com.manniu.screen.mapper.LockScreenUserLockPermissionsMapper;
import com.manniu.screen.service.ILockScreenUserLockKeyService;
import com.manniu.screen.service.ILockScreenUserLockPermissionsService;
import com.manniu.screen.vo.CommonVO;
import com.manniu.screen.vo.param.LockScreenUserLockPermissionsPageParamVO;
import com.manniu.screen.vo.view.LockScreenUserLockPermissionsViewVO;
import com.ruoyi.common.exception.ServiceException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LockScreenUserLockPermissionsImpl extends
    ServiceImpl<LockScreenUserLockPermissionsMapper, LockScreenUserLockPermissions> implements
    ILockScreenUserLockPermissionsService {

    private final LockScreenUserLockPermissionsMapper userLockPermissionsMapper;
    private final ILockScreenUserLockKeyService userLockKeyService;

    private final LockTemplate lockTemplate;

    @Override
    public Map<String, Object> mySaveOrUpdate(
        List<LockScreenUserLockPermissions> lockPermissionsList) {
        LockScreenUserLockKey userLockKey = userLockKeyService.getByUserId(
            lockPermissionsList.get(0).getUserId());
        if (userLockKey == null) {
            throw new ServiceException("授权用户还未录入卡号或指纹");
        }
        List<String> errorList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (LockScreenUserLockPermissions item : lockPermissionsList) {
                boolean success = setLock(item, userLockKey, item.getStartTime().format(formatter),
                    item.getEndTime().format(formatter));
                if (success) {
                    save(item);
                } else {
                    errorList.add(item.getIp() + CommonConst.ENGLISH_COLON + item.getDeviceId()
                        + CommonConst.ENGLISH_COLON + item.getLockId() + "授权失败");
                }
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", errorList.size() > 0 ? "-1" : "200");
        resultMap.put("data", errorList);
        return resultMap;
    }

    @Override
    public List<LockScreenUserLockPermissionsViewVO> getUserLockPermissionsList(
        LockScreenUserLockPermissionsPageParamVO pageParamVO) {
        return userLockPermissionsMapper.selectUserLockPermissionsList(pageParamVO);
    }

    @Override
    public int removeByUserId(CommonVO commonVO) {
        LockScreenUserLockPermissions lockPermissions = getById(commonVO.getId());
        try {
            LockResult lockResult = lockTemplate.removeAccount(lockPermissions.getIp(),
                lockPermissions.getDeviceId(), commonVO.getLockId(),
                lockPermissions.getUserId().toString());
            return lockResult.getB() ? userLockPermissionsMapper.deleteByDataId(
                lockPermissions.getId()) : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    private boolean setLock(LockScreenUserLockPermissions item, LockScreenUserLockKey userLockKey,
        String startTime, String endTime) {
        String userId = item.getUserId().toString();
        try {
            lockTemplate.addAccount(item.getIp(), item.getDeviceId(), item.getElectronicLockId(),
                userId);
            if (StringUtils.isNotBlank(userLockKey.getCarNumber())) {
                lockTemplate.setIcNumber(item.getIp(), item.getDeviceId(),
                    item.getElectronicLockId(), userId, userLockKey.getCarNumber());
            }
            if (StringUtils.isNotBlank(userLockKey.getPassword())) {
                lockTemplate.setPass(item.getIp(), item.getDeviceId(), item.getElectronicLockId(),
                    userId, userLockKey.getPassword());
            }
            if (StringUtils.isNotBlank(userLockKey.getFingerprint())) {
                lockTemplate.setFingerprint(item.getIp(), item.getDeviceId(),
                    item.getElectronicLockId(), userId, userLockKey.getFingerprint());
            }
            return lockTemplate.setUserOpenTime(item.getIp(), item.getDeviceId(),
                item.getElectronicLockId(), userId, 1, startTime, endTime).getB();

        } catch (Exception e) {
            return false;
        }
    }
}
