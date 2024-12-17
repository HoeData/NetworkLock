package com.manniu.screen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinfu.lock.core.LockTemplate;
import com.manniu.screen.constans.CommonConst;
import com.manniu.screen.domain.LockScreenUserLockKey;
import com.manniu.screen.enums.UserLockKeyType;
import com.manniu.screen.mapper.LockScreenUserLockKeyMapper;
import com.manniu.screen.mapper.LockScreenUserLockPermissionsMapper;
import com.manniu.screen.service.ILockScreenUserLockKeyService;
import com.manniu.screen.utils.CommonUtils;
import com.manniu.screen.vo.param.LockScreenUserLockPermissionsPageParamVO;
import com.manniu.screen.vo.param.LockUserKeyPageParamVO;
import com.manniu.screen.vo.view.LockScreenUserLockPermissionsViewVO;
import com.manniu.screen.vo.view.LockUserKeyViewVO;
import com.ruoyi.common.core.domain.AjaxResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LockScreenUserLockKeyServiceImpl extends
    ServiceImpl<LockScreenUserLockKeyMapper, LockScreenUserLockKey> implements
    ILockScreenUserLockKeyService {

    private final LockScreenUserLockKeyMapper userLockKeyMapper;
    private final LockScreenUserLockPermissionsMapper userLockPermissionsMapper;
    private final LockTemplate lockTemplate;

    @Override
    public LockScreenUserLockKey getByUserId(Integer userId) {
        LambdaQueryWrapper<LockScreenUserLockKey> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LockScreenUserLockKey::getId, userId);
        queryWrapper.eq(LockScreenUserLockKey::getDelFlag, CommonConst.ZERO_STR);
        return getOne(queryWrapper);
    }

    @Override
    public boolean mySaveOrUpdate(LockScreenUserLockKey lockScreenUserLockKey) {
        if (null != lockScreenUserLockKey.getId() && null != lockScreenUserLockKey.getType()) {
            UserLockKeyType keyType = UserLockKeyType.getEnum(lockScreenUserLockKey.getType());
            LockScreenUserLockKey old = getByUserId(lockScreenUserLockKey.getId());
            judgeCardNumber(lockScreenUserLockKey.getCardNumber(), lockScreenUserLockKey.getId());
            judgePassword(lockScreenUserLockKey.getPassword(), lockScreenUserLockKey.getId());
            switch (keyType) {
                case PASSWORD:
                    old.setPassword(lockScreenUserLockKey.getPassword());
                    break;
                case CARD_NUMBER:
                    old.setCardNumber(lockScreenUserLockKey.getCardNumber());
                    break;
                case FINGERPRINT:
                    old.setFingerprint(lockScreenUserLockKey.getFingerprint());
                    break;
            }
            lockScreenUserLockKey = old;
        }
        CommonUtils.addCommonParams(lockScreenUserLockKey, lockScreenUserLockKey.getId());
        boolean result;
        synchronized (this) {
            if (null == lockScreenUserLockKey.getId()) {
                lockScreenUserLockKey.setRealAccountId(getAccountId());
            }
            result = saveOrUpdate(lockScreenUserLockKey);
        }
        return result;
    }

    @Override
    public List<LockUserKeyViewVO> getUserLockKeyList(LockUserKeyPageParamVO pageParamVO) {
        List<LockUserKeyViewVO> list = userLockKeyMapper.selectUserLockKeyList(pageParamVO);
        list.forEach(item -> {
            item.setCardNumberStatus(
                StringUtils.isNotBlank(item.getCardNumber()) ? CommonConst.ONE : CommonConst.ZERO);
            item.setPasswordStatus(
                StringUtils.isNotBlank(item.getPassword()) ? CommonConst.ONE : CommonConst.ZERO);
            item.setFingerprintStatus(
                StringUtils.isNotBlank(item.getFingerprint()) ? CommonConst.ONE : CommonConst.ZERO);
            item.setCardNumber(null);
            item.setPassword(null);
            item.setFingerprint(null);
        });
        return list;
    }

    @Override
    public AjaxResult deleteByUserId(Integer userId) {
        LockScreenUserLockKey lockScreenUserLockKey = getByUserId(userId);
        String accountId = lockScreenUserLockKey.getRealAccountId().toString();
        List<LockScreenUserLockPermissionsViewVO> list = userLockPermissionsMapper.selectUserLockPermissionsList(
            LockScreenUserLockPermissionsPageParamVO.builder().userId(userId).build());
        if (list.size() > 0) {
            List<String> errorList = new ArrayList<>();
            for (LockScreenUserLockPermissionsViewVO item : list) {
                try {
                    if (!lockTemplate.removeAccount(item.getIp(), item.getDeviceId(),
                        item.getLockId(), accountId).getB()) {
                        errorList.add(item.getIp() + CommonConst.ENGLISH_COLON + item.getDeviceId()
                            + CommonConst.ENGLISH_COLON + item.getLockId() + "删除授权失败");
                    }
                } catch (Exception e) {
                    errorList.add(item.getIp() + CommonConst.ENGLISH_COLON + item.getDeviceId()
                        + CommonConst.ENGLISH_COLON + item.getLockId() + "删除授权失败");
                }
            }
            if (errorList.size() > 0) {
                return AjaxResult.error().put("data", errorList);
            }
        }
        userLockKeyMapper.deleteByDataId(userId);
        return AjaxResult.success();
    }

    private int getAccountId() {
        int max = 300;
        LambdaQueryWrapper<LockScreenUserLockKey> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(LockScreenUserLockKey::getDelFlag, CommonConst.ZERO_STR);
        List<LockScreenUserLockKey> list = list(lambdaQueryWrapper);
        if (list.size() >= max) {
            throw new RuntimeException("用户已满,无法新增用户");
        }
        Set<Integer> set = list.stream().map(LockScreenUserLockKey::getRealAccountId)
            .collect(Collectors.toSet());
        int accountId = 0;
        for (int i = 1; i <= max; i++) {
            if (!set.contains(i)) {
                accountId = i;
                break;
            }
        }
        return accountId;
    }

    private void judgeCardNumber(String cardNumber, Integer userId) {
        if (StringUtils.isNotBlank(cardNumber)) {
            if (cardNumber.length() != 8) {
                throw new RuntimeException("IC卡号错误");
            }
            LambdaQueryWrapper<LockScreenUserLockKey> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(LockScreenUserLockKey::getCardNumber, cardNumber);
            lambdaQueryWrapper.eq(LockScreenUserLockKey::getDelFlag, CommonConst.ZERO_STR);
            LockScreenUserLockKey old = getOne(lambdaQueryWrapper);
            if (!(null == old || old.getId().equals(userId))) {
                throw new RuntimeException("IC卡已占用");
            }
        }

    }

    private void judgePassword(String password, Integer userId) {
        if (StringUtils.isNotBlank(password)) {
            if (password.length() != 6) {
                throw new RuntimeException("密码固定为6位");
            }
            LambdaQueryWrapper<LockScreenUserLockKey> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(LockScreenUserLockKey::getPassword, password);
            lambdaQueryWrapper.eq(LockScreenUserLockKey::getDelFlag, CommonConst.ZERO_STR);
            LockScreenUserLockKey old = getOne(lambdaQueryWrapper);
            if (!(null == old || old.getId().equals(userId))) {
                throw new RuntimeException("密码已被占用");
            }
        }
    }
}
