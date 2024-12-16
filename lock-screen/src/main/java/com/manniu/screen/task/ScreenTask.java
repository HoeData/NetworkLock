package com.manniu.screen.task;

import com.jinfu.lock.core.LockTemplate;
import com.manniu.screen.config.LockScreenCache;
import com.manniu.screen.service.ILockScreenNetworkControlService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@Slf4j
@RequiredArgsConstructor
public class ScreenTask {

    private final ILockScreenNetworkControlService networkControlService;
    private final LockTemplate lockTemplate;

    @Scheduled(cron = "* 5 * * * ?")
    public void initOnlineAll() {
        if (LockScreenCache.init) {
            return;
        }
        Map<String, List<String>> dataMap = lockTemplate.onlineAll();
        if (dataMap.size() > 0) {
            LockScreenCache.init = true;
            networkControlService.saveAndUpdateControlAndLockByMap(dataMap);
        }
    }
}
