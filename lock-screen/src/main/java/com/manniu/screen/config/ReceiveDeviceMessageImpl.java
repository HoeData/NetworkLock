package com.manniu.screen.config;

import com.alibaba.fastjson2.JSON;
import com.jinfu.lock.callback.ObServer;
import com.jinfu.lock.pojo.OpenLockMessage;

public class ReceiveDeviceMessageImpl implements ObServer {

    @Override
    public void heart(OpenLockMessage openLockMessage) {
        System.out.println(JSON.toJSONString(openLockMessage));
    }

    @Override
    public void offlineLock(OpenLockMessage openLockMessage) {
        System.out.println(JSON.toJSONString(openLockMessage));

    }

    @Override
    public void openLock(OpenLockMessage openLockMessage) {
        System.out.println(JSON.toJSONString(openLockMessage));

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
