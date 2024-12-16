package com.ruoyi.web.task;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.license.LicenseParamVO;
import com.ruoyi.common.license.LockLicenseInfoVO;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.web.constants.LockCache;
import com.ruoyi.web.core.config.LicenseProperties;
import com.ruoyi.web.domain.LockInfo;
import com.ruoyi.web.service.ILockInfoService;
import com.ruoyi.web.utils.GetStartLicenseUtil;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    public static final String PRIVATE_KEY =
        "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALGsZKvnqHNtJFuj"
            + "9qCwNblC+dVIE010vGNgy8/atzFKXVEYHJbYeU24T5HA3aEiFMkcnjtSIcTzKZY7"
            + "6X2k6M2HWUt1YBQj7OwO3jSihdsB8ArrNs/FAquqoU+PxuwG5DvL2YiHtJNt1Nye"
            + "yJ3vfP34X8350071X7Hqp1KUZ4ohAgMBAAECgYBNxjkLRwzl+hDATLXZAUmDH15d"
            + "jn9kmIUeu3B8PDGU0ginRva80WXIL7YlB1f9AP44St64+OrvW8IIkZFT/qwpEJDs"
            + "yMPiJmjRyGPr62iqsnG82e+b/D59hZgrIe1Sb6tdUbUh6J4+4K2HbIdY1t6jPWRr"
            + "ysE413l5MnJQqZSAAQJBAOdYRqOXZXmoIRWP0lKGdmo92PfhiJrzoruYZDkQXx7h"
            + "E4NL1b0AY5HOev3+KkozO/zqWtqVpj3nXVFXUqJxa0ECQQDEm9Aw3iTvj4j/90BW"
            + "YbZODgHfhMUhipfRSU4xg/ffm6uqMQDpQcFb/FCMPekq3HFZMfoN2UEgGvKwoBH4"
            + "xMbhAkAC6m/pe+0BfYb9OJTUCXHQoPrtFOCd41g3uRH6TiSExR1z2C7XdPvMSKfw"
            + "L5Xk3YRyCZofiydPPG1Gqy0VcwyBAkBflPR43XaNdHWBIz4HAMf1WH/2n4CK1usJ"
            + "1x6JmgPGlNK3Ec3EmLAdPSQXmf2iVbtRRqevVeCAcDluPtOd4mRhAkEAvJRIlDON"
            + "b1nSSQShT6Y/cHGh37MLfo6hKO1gqPwlyGoIay941P9hl2MSgjYUp0aVAe/RUQdI"
            + "veM6K6605Eo0gA==";

    @Override
    public void run(ApplicationArguments args) {
        LicenseProperties licenseProperties = SpringUtils.getBean(LicenseProperties.class);
        try {
            judgeStartLicense(licenseProperties.getStartLicensePath());
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }

        ILockInfoService lockInfoService = SpringUtils.getBean(ILockInfoService.class);
        if (licenseProperties.getLockLicensePathList().size() == 0) {
            log.error("InitLicenseRunner-run:license路径为空,初始化license失败");
        }
        try {
            for (String path : licenseProperties.getLockLicensePathList()) {
                LicenseParamVO licenseParamVO = getLicenseParamVO(readFile(path));
                List<LockLicenseInfoVO> list = licenseParamVO.getLockInfoList();
                LockCache.licenseParamVOList.add(licenseParamVO);
                LockCache.lockNumber += licenseParamVO.getLockNumber();
                List<LockInfo> lockInfoList = new ArrayList<>();
                for (LockLicenseInfoVO lockInfoVO : list) {
                    LockCache.lockSerialNumberSet.add(lockInfoVO.getSerialNumber());
                    if (StringUtils.isNotBlank(lockInfoVO.getSerialNumber())) {
                        LockInfo lockInfo = new LockInfo();
                        lockInfo.setSerialNumber(lockInfoVO.getSerialNumber());
                        lockInfo.setBatchNo(licenseParamVO.getBatchNo());
                        lockInfo.setType(lockInfoVO.getType());
                        lockInfoList.add(lockInfo);
                    }

                }
                lockInfoService.saveOrUpdateBatch(lockInfoList);
            }
            log.info("初始化license成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("InitLicenseRunner-run:初始化license失败" + e.getMessage());
        }
    }

    private String readFile(String filePath) {
        log.info("filePath=" + filePath);
        String value = "";
        try (Stream<String> publicLines = Files.lines(Paths.get(filePath),
            StandardCharsets.UTF_8)) {
            value = publicLines.collect(Collectors.joining(""));
        } catch (IOException e) {
            e.getMessage();
        }
        LockCache.licenseStrList.add(value);
        return value;
    }

    private LicenseParamVO getLicenseParamVO(String licenseValue) {
        RSA rsa = new RSA(PRIVATE_KEY, null);
        return JSONObject.parseObject(rsa.decryptStr(licenseValue, KeyType.PrivateKey),
            LicenseParamVO.class);
    }

    public static boolean isWin() {
        String osName = System.getProperty("os.name");
        // 判断是否为Windows系统
        return osName.toLowerCase().contains("windows");
    }

    private void judgeStartLicense(String path) {
//        RSA rsa = new RSA(PRIVATE_KEY, null);
//        StartLicenseVO startLicenseVO = JSONObject.parseObject(
//            rsa.decryptStr(readFile(path), KeyType.PrivateKey), StartLicenseVO.class);
//        if (startLicenseVO.getTemporaryAuthorization()) {
//            if (startLicenseVO.getEndTime().isBefore(LocalDateTime.now())) {
//                throw new RuntimeException("license校验失败,已过有效期,请重新申请");
//            }
//            return;
//        }
//        if (!StringUtils.equals(startLicenseVO.getProductId(),
//            GetStartLicenseUtil.getCreateLicenseStr())) {
//            throw new RuntimeException("license校验失败,请重新申请");
//        }
//        if (startLicenseVO.getEndTime().isBefore(LocalDateTime.now())) {
//            throw new RuntimeException("license校验失败,已过有效期,请重新申请");
//        }
//        log.info("初始化license成功,有效期截止="+startLicenseVO.getEndTime());
    }


}
