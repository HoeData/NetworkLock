package com.manniu.screen.config;

import com.alibaba.fastjson2.JSON;
import com.jinfu.lock.callback.ObServer;
import com.jinfu.lock.pojo.OpenLockMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReceiveDeviceMessageImpl implements ObServer {

    @Override
    public void heart(OpenLockMessage openLockMessage) {
       log.info("heart="+JSON.toJSONString(openLockMessage));
    }

    @Override
    public void offlineLock(OpenLockMessage openLockMessage) {
        log.info("offlineLock="+JSON.toJSONString(openLockMessage));
    }

    @Override
    public void openLock(OpenLockMessage openLockMessage) {
        log.info("openLock="+JSON.toJSONString(openLockMessage));
    }

    @Override
    public void report(OpenLockMessage openLockMessage) {
        System.out.println(JSON.toJSONString(openLockMessage));

    }

    @Override
    public void returnMsg(String s, byte[] bytes, byte b) {

    }

    @Override
    public void webMsg(String s) {

    }
}
