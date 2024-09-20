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
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InitLicenseRunner implements ApplicationRunner {

    private static final String PUBLIC_KEY =
        "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCxrGSr56hzbSRbo/agsDW5QvnV\n"
            + "SBNNdLxjYMvP2rcxSl1RGByW2HlNuE+RwN2hIhTJHJ47UiHE8ymWO+l9pOjNh1lL\n"
            + "dWAUI+zsDt40ooXbAfAK6zbPxQKrqqFPj8bsBuQ7y9mIh7STbdTcnsid73z9+F/N\n"
            + "+dNO9V+x6qdSlGeKIQIDAQAB";

    @Override
    public void run(ApplicationArguments args) {
        LicenseProperties licenseProperties = SpringUtils.getBean(LicenseProperties.class);
        if (licenseProperties.getPathList().size() == 0) {
            log.error("InitLicenseRunner-run:license路径为空,初始化license失败");
        }
        try {
            for (String path : licenseProperties.getPathList()) {
                LicenseParamVO licenseParamVO = getLicenseParamVO(readFile(path));
                LockCache.lockSerialNumberSet.addAll(licenseParamVO.getLockSerialNumberList());
                LockCache.licenseParamVOList.add(licenseParamVO);
                LockCache.lockNumber += licenseParamVO.getLockNumber();
            }
            log.info("初始化license成功");
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
        RSA rsa = new RSA(null, PUBLIC_KEY);
        return JSONObject.parseObject(rsa.decryptStr(licenseValue, KeyType.PublicKey),
            LicenseParamVO.class);
    }
}
