package com.ruoyi.web.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PdaDataSynchronizationStopThread implements Runnable {

    public volatile static boolean RUNNING = true;

    @Override
    public void run() {
        while (RUNNING) {
            if (!PdaDataSynchronizationUtil.RUNNING) {
                PdaDataSynchronizationUtil.RUNNING = true;
                try {
                    PdaDataSynchronizationUtil.startPdaDataSynchronization();
                } catch (Exception e) {
                    RUNNING = false;
                    log.error("FlagStopThread-error=" + e.getMessage());
                }
            }
        }
        log.info("线程退出了。");
        if(!PdaDataSynchronizationUtil.statusVO.getEndFlag()){
            PdaDataSynchronizationUtil.statusVO.setErrorFlag(true);
            PdaDataSynchronizationUtil.writeStatusToPda();
        }
        PdaDataSynchronizationUtil.RUNNING = false;
    }
    // 提供一个方法来请求线程停止
}
