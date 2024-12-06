package com.manniu.screen.utils;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.LockEntity;
import java.util.Date;

public class CommonUtils {

    public static void addCommonParams(Object entity,Integer id) {
        if (entity instanceof LockEntity) {
            LockEntity lockEntity = ((LockEntity) entity);
            String userId = "";
            try {
                userId = (SecurityUtils.getLoginUser().getUserId().toString());
            } catch (Exception e) {

            }
            if (null == id) {
                lockEntity.setCreateBy(userId);
                lockEntity.setCreateTime(new Date());
            }
            lockEntity.setUpdateBy(userId);
            lockEntity.setUpdateTime(new Date());
        }
    }
    public static void addCommonParams(Object entity) {
        addCommonParams(entity,null);
    }
}
