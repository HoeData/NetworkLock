package com.manniu.screen.utils;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.LockEntity;
import java.util.Date;

public class CommonUtils {

    public static void addCommonParams(Object entity,Integer id) {
        if (entity instanceof LockEntity) {
            LockEntity lockEntity = ((LockEntity) entity);
            if(null==id){
                lockEntity.setCreateBy(SecurityUtils.getLoginUser().getUserId().toString());
                lockEntity.setCreateTime(new Date());
            }
            lockEntity.setUpdateBy(SecurityUtils.getLoginUser().getUserId().toString());
            lockEntity.setUpdateTime(new Date());
        }
    }
    public static void addCommonParams(Object entity) {
        addCommonParams(entity,null);
    }
}
