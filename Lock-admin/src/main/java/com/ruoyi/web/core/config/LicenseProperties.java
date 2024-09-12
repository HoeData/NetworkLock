package com.ruoyi.web.core.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class LicenseProperties {
    @Value("${license.path}")
    private String path;
}
