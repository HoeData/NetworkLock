package com.ruoyi.web.task;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.license.LicenseParamVO;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.web.constants.LockCache;
import com.ruoyi.web.core.config.LicenseProperties;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InitLicenseRunner implements ApplicationRunner {


    private static final String PUBLIC_KEY =
        "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCLCOITP83Y/pDHXa0n/d4TANQsuCKycK1x0b5QUU0sm2sbGXluZmbg8kwSk"
            + "YaQgB0mfITdkT/nnskPghospYp7BQL8p5eBESUSOq+rBai/+8ZddfN4kv15b/WX4KCrs1vY4CbBW7OgRvVRm+WdsP9Aw45R5Q71mSqNmSyd5/xv3wIDAQAB";

    @Override
    public void run(ApplicationArguments args) throws Exception {
        LicenseProperties licenseProperties = SpringUtils.getBean(LicenseProperties.class);
        if (StringUtils.isBlank(licenseProperties.getPath())) {
            log.error("InitLicenseRunner-run:license路径为空,初始化license失败");
        }
        try {
            LicenseParamVO licenseParamVO = getLicenseParamVO(
                readFile(licenseProperties.getPath()));
            LockCache.lockSerialNumberSet.addAll(licenseParamVO.getLockSerialNumberList());
            LockCache.licenseParamVO = licenseParamVO;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("InitLicenseRunner-run:初始化license失败" + e.getMessage());
        }
    }

    private String readFile(String filePath) {
        String value = "";
        try (Stream<String> publicLines = Files.lines(Paths.get(filePath),
            StandardCharsets.UTF_8)) {
            value = publicLines.collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.getMessage();
        }
        return value;
    }

    private LicenseParamVO getLicenseParamVO(String licenseValue) {
        LicenseParamVO licenseParamVO = new LicenseParamVO();
        RSA rsa = new RSA(null, PUBLIC_KEY);
        return JSONObject.parseObject(rsa.decryptStr(licenseValue, KeyType.PublicKey),
            LicenseParamVO.class);
    }
}
