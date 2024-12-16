package com.ruoyi.web.core.config;

import java.util.List;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "license")
public class LicenseProperties {
    private List<String> lockLicensePathList;
    private String startLicensePath;
}
