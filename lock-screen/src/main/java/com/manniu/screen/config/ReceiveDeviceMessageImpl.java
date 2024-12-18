package com.manniu.screen.config;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSON;
import com.jinfu.lock.callback.ObServer;
import com.jinfu.lock.pojo.OpenLockMessage;
import com.manniu.screen.constans.CommonConst;
import com.manniu.screen.domain.LockScreenUnlock;
import com.manniu.screen.service.ILockScreenUnlockService;
import com.manniu.screen.vo.LockStatusVO;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

@Slf4j
public class ReceiveDeviceMessageImpl implements ObServer {

    public static final String HORIZONTAL_LINE = "-";
    public static final String OFFLINE = "离线";
    public static final String ONLINE = "在线";


    @Override
    public void heart(OpenLockMessage openLockMessage) {
        log.info("heart=" + JSON.toJSONString(openLockMessage));
        LockScreenCache.deviceStatusVOMap.put(openLockMessage.getDeviceId(), ONLINE);
        Map<String, LockStatusVO> map = LockScreenCache.lockStatusVOMap.get(
            openLockMessage.getDeviceId());
        if (CommonConst.ZERO_STR.equals(openLockMessage.getLockId())) {
            if (null != map) {
                map.forEach((k, v) -> {
                    v.setDoorStatus(HORIZONTAL_LINE);
                    v.setLockStatus(HORIZONTAL_LINE);
                    v.setOnlineStatus(OFFLINE);
                });
            }
            return;
        }
        if (null == map) {
            map = new HashMap<>();
        }
        String lockStatus = openLockMessage.getLockStatus();
        if (StringUtils.isBlank(lockStatus)) {
            LockStatusVO lockStatusVO = map.get(openLockMessage.getLockId());
            lockStatusVO.setDoorStatus(HORIZONTAL_LINE);
            lockStatusVO.setLockStatus(HORIZONTAL_LINE);
            lockStatusVO.setOnlineStatus(OFFLINE);
            map.put(openLockMessage.getLockId(), lockStatusVO);
            LockScreenCache.lockStatusVOMap.put(openLockMessage.getDeviceId(), map);
            return;
        }
        LockStatusVO lockStatusVO = map.getOrDefault(openLockMessage.getLockId(),
            new LockStatusVO());
        lockStatusVO.setDoorStatus(
            LockScreenCache.doorStatus.getOrDefault(openLockMessage.getDoorStatus(),
                HORIZONTAL_LINE));
        lockStatusVO.setLockStatus(
            LockScreenCache.lockStatus.getOrDefault(openLockMessage.getLockStatus(),
                HORIZONTAL_LINE));
        lockStatusVO.setOnlineStatus(ONLINE);
        map.put(openLockMessage.getLockId(), lockStatusVO);
        LockScreenCache.lockStatusVOMap.put(openLockMessage.getDeviceId(), map);
    }

    @Override
    public void offlineLock(OpenLockMessage openLockMessage) {
        log.info("offlineLock=" + JSON.toJSONString(openLockMessage));
        if (StringUtils.equals("3", openLockMessage.getType()) && StringUtils.equals("2",
            openLockMessage.getStatus())) {
            LockScreenCache.deviceStatusVOMap.put(openLockMessage.getDeviceId(), OFFLINE);
            Map<String, LockStatusVO> map = LockScreenCache.lockStatusVOMap.get(
                openLockMessage.getDeviceId());
            if (null != map) {
                map.forEach((k, v) -> {
                    v.setDoorStatus(HORIZONTAL_LINE);
                    v.setLockStatus(HORIZONTAL_LINE);
                    v.setOnlineStatus(OFFLINE);
                });
            }
        }
    }

    @Override
    public void openLock(OpenLockMessage openLockMessage) {
        log.info("openLock="+JSON.toJSONString(openLockMessage));
    }

    @SneakyThrows
    @Override
    public void report(OpenLockMessage openLockMessage) {
        log.info("report=" + JSON.toJSONString(openLockMessage));
        if (openLockMessage.getType().equals("7") && LockScreenCache.codeMap.containsKey(
            openLockMessage.getStatus())) {
            SpringUtil.getBean(ILockScreenUnlockService.class).save(
                LockScreenUnlock.builder().deviceId(openLockMessage.getDeviceId())
                    .lockId(openLockMessage.getLockId()).type(openLockMessage.getStatus())
                    .createTime(new Date())
                    .lockTime(DateUtils.parseDate(openLockMessage.getTime(), "yyyy-MM-dd HH:mm:ss"))
                    .passwordOrNumber(openLockMessage.getStr())
                    .userId(openLockMessage.getAccountId()).build());
        }
    }

    @Override
    public void returnMsg(String s, byte[] bytes, byte b) {

    }

    @Override
    public void webMsg(String s) {

    }
}
