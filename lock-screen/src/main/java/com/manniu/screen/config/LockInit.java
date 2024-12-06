package com.manniu.screen.config;

import com.jinfu.lock.mina.MinaServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * mina服务器初始化
 */
@Component
public class LockInit extends MinaServer implements ApplicationRunner {
    @Autowired
    private MinaServer minaServer;

    @Override
    public void run(ApplicationArguments args) {
        minaServer.init(8234);
        ReceiveDeviceMessageImpl receiveDeviceMessage = new ReceiveDeviceMessageImpl();
        minaServer.MY_OBSERVER_LIST.add(receiveDeviceMessage);
    }
}


