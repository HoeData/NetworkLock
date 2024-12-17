package com.manniu.screen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
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
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.exception.ServiceException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
    public AjaxResult mySaveOrUpdate(
        List<LockScreenUserLockPermissions> lockPermissionsList) {
        LockScreenUserLockKey userLockKey = userLockKeyService.getByUserId(
            lockPermissionsList.get(CommonConst.ZERO).getUserId());
        if (userLockKey == null) {
            throw new ServiceException("授权用户还未录入卡号或指纹");
        }
        String errorMsg = "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (LockScreenUserLockPermissions item : lockPermissionsList) {
            boolean success = setLock(item, userLockKey, item.getStartTime().format(formatter),
                item.getEndTime().format(formatter));
            if (success) {
                item.setAuthorizationFlag(CommonConst.ONE);
            } else {
                errorMsg += item.getIp() + CommonConst.ENGLISH_COLON + item.getDeviceId()
                    + CommonConst.ENGLISH_COLON + item.getLockId() + "授权失败,";
            }
            saveOrUpdate(item);
        }
        if (errorMsg.length() > CommonConst.ZERO) {
            return AjaxResult.error(
                errorMsg.substring(CommonConst.ZERO, errorMsg.length() - CommonConst.ONE));
        }
        return AjaxResult.success();
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
            LockResult lockResult = lockTemplate.removeAccount(commonVO.getIp(),
                commonVO.getDeviceId(), commonVO.getLockId(),
                userLockKeyService.getByUserId(lockPermissions.getUserId()).getRealAccountId()
                    .toString());
            return lockResult.getB() ? userLockPermissionsMapper.deleteByDataId(
                lockPermissions.getId()) : CommonConst.ZERO;
        } catch (Exception e) {
            return CommonConst.ZERO;
        }
    }

    @Override
    public AjaxResult updateByUserId(Integer userId) {
        PageHelper.startPage(1, Integer.MAX_VALUE);
        List<LockScreenUserLockPermissionsViewVO> list = getUserLockPermissionsList(
            LockScreenUserLockPermissionsPageParamVO.builder().userId(userId).build());
        if (list.size() > CommonConst.ZERO) {
            List<LockScreenUserLockPermissions> lockPermissionsList = new ArrayList<>();
            LockScreenUserLockPermissions lockPermissions;
            for (LockScreenUserLockPermissionsViewVO item : list) {
                lockPermissions = new LockScreenUserLockPermissions();
                lockPermissions.setUserId(userId);
                lockPermissions.setId(item.getId());
                lockPermissions.setElectronicLockId(item.getElectronicLockId());
                lockPermissions.setLockId(item.getLockId());
                lockPermissions.setIp(item.getIp());
                lockPermissions.setDeviceId(item.getDeviceId());
                lockPermissions.setStartTime(item.getStartTime());
                lockPermissions.setEndTime(item.getEndTime());
                lockPermissionsList.add(lockPermissions);
            }
            return mySaveOrUpdate(lockPermissionsList);
        }
        return AjaxResult.success();

    }

    private boolean setLock(LockScreenUserLockPermissions item, LockScreenUserLockKey userLockKey,
        String startTime, String endTime) {
        String userId = userLockKey.getRealAccountId().toString();
        try {
            lockTemplate.removeAccount(item.getIp(), item.getDeviceId(), item.getElectronicLockId(),
                userId);
            if (!lockTemplate.addAccount(item.getIp(), item.getDeviceId(),
                item.getElectronicLockId(), userId).getB()) {
                return false;
            }
            if (StringUtils.isNotBlank(userLockKey.getCardNumber())) {
                if (!lockTemplate.setIcNumber(item.getIp(), item.getDeviceId(),
                    item.getElectronicLockId(), userId, userLockKey.getCardNumber()).getB()) {
                    return false;
                }
            }
            if (StringUtils.isNotBlank(userLockKey.getPassword())) {
                if (!lockTemplate.setPass(item.getIp(), item.getDeviceId(),
                    item.getElectronicLockId(), userId, userLockKey.getPassword()).getB()) {
                    return false;
                }
            }
            if (StringUtils.isNotBlank(userLockKey.getFingerprint())) {
                if (!lockTemplate.setFingerprint(item.getIp(), item.getDeviceId(),
                    item.getElectronicLockId(), userId, userLockKey.getFingerprint()).getB()) {
                    return false;
                }
            }
            return lockTemplate.setUserOpenTime(item.getIp(), item.getDeviceId(),
                item.getElectronicLockId(), userId, 1, startTime, endTime).getB();

        } catch (Exception e) {
            return false;
        }
    }
}
