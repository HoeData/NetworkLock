package com.manniu.screen.config;

import com.jinfu.lock.core.LockTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LockBean {

    @Bean
    public LockTemplate getLockTemplate() {
        return new LockTemplate();
    }


}
