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
import com.ruoyi.web.utils.PdaDataSynchronizationUtil;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    public static String JAR_PATH = "";
    public static final String PRIVATE_KEY =
        "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALGsZKvnqHNtJFuj\n"
            + "9qCwNblC+dVIE010vGNgy8/atzFKXVEYHJbYeU24T5HA3aEiFMkcnjtSIcTzKZY7\n"
            + "6X2k6M2HWUt1YBQj7OwO3jSihdsB8ArrNs/FAquqoU+PxuwG5DvL2YiHtJNt1Nye\n"
            + "yJ3vfP34X8350071X7Hqp1KUZ4ohAgMBAAECgYBNxjkLRwzl+hDATLXZAUmDH15d\n"
            + "jn9kmIUeu3B8PDGU0ginRva80WXIL7YlB1f9AP44St64+OrvW8IIkZFT/qwpEJDs\n"
            + "yMPiJmjRyGPr62iqsnG82e+b/D59hZgrIe1Sb6tdUbUh6J4+4K2HbIdY1t6jPWRr\n"
            + "ysE413l5MnJQqZSAAQJBAOdYRqOXZXmoIRWP0lKGdmo92PfhiJrzoruYZDkQXx7h\n"
            + "E4NL1b0AY5HOev3+KkozO/zqWtqVpj3nXVFXUqJxa0ECQQDEm9Aw3iTvj4j/90BW\n"
            + "YbZODgHfhMUhipfRSU4xg/ffm6uqMQDpQcFb/FCMPekq3HFZMfoN2UEgGvKwoBH4\n"
            + "xMbhAkAC6m/pe+0BfYb9OJTUCXHQoPrtFOCd41g3uRH6TiSExR1z2C7XdPvMSKfw\n"
            + "L5Xk3YRyCZofiydPPG1Gqy0VcwyBAkBflPR43XaNdHWBIz4HAMf1WH/2n4CK1usJ\n"
            + "1x6JmgPGlNK3Ec3EmLAdPSQXmf2iVbtRRqevVeCAcDluPtOd4mRhAkEAvJRIlDON\n"
            + "b1nSSQShT6Y/cHGh37MLfo6hKO1gqPwlyGoIay941P9hl2MSgjYUp0aVAe/RUQdI\n"
            + "veM6K6605Eo0gA==";

    @Override
    public void run(ApplicationArguments args) {
        // 获取当前类的Class对象
        Class<?> clazz = InitLicenseRunner.class;

        try {
            // 通过类加载器获取当前类的资源
            URL resource = clazz.getProtectionDomain().getCodeSource().getLocation();

            // 将URL转换为URI
            URI uri = resource.toURI();

            // 处理可能的jar:file协议
            if (uri.getScheme().equals("jar")) {
                // 如果是jar:file协议，我们需要去掉前缀"jar:"和"!/"
                String jarFilePath = uri.toString().substring(5, uri.toString().indexOf("!/"));
                File jarFile = new File(jarFilePath);
                log.error("JAR file path: " + jarFile.getAbsolutePath());
                // 获取JAR文件所在的目录
                JAR_PATH = jarFile.getParentFile().getAbsolutePath().split("ile:")[1];
                if(isWin()){
                    JAR_PATH = JAR_PATH.substring(1);
                }
                log.error("JAR directory: " + JAR_PATH);
            } else {
                // 对于普通的文件路径
                File file = new File(uri);
                JAR_PATH=file.getAbsolutePath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        PdaDataSynchronizationUtil.ADB_PATH = JAR_PATH+File.separator+"platform-tools"+File.separator+"adb.exe";
        LicenseProperties licenseProperties = SpringUtils.getBean(LicenseProperties.class);
        ILockInfoService lockInfoService = SpringUtils.getBean(ILockInfoService.class);
        if (licenseProperties.getPathList().size() == 0) {
            log.error("InitLicenseRunner-run:license路径为空,初始化license失败");
        }
        try {
            for (String path : licenseProperties.getPathList()) {
                LicenseParamVO licenseParamVO = getLicenseParamVO(readFile(
                    JAR_PATH + File.separator + "license" + File.separator
                        + PdaDataSynchronizationUtil.getFileName(path)));
                List<LockLicenseInfoVO> list = licenseParamVO.getLockInfoList();
                LockCache.licenseParamVOList.add(licenseParamVO);
                LockCache.lockNumber += licenseParamVO.getLockNumber();
                List<LockInfo> lockInfoList = new ArrayList<>();
                for (LockLicenseInfoVO lockInfoVO : list) {
                    LockCache.lockSerialNumberSet.add(lockInfoVO.getSerialNumber());
                    if(StringUtils.isNotBlank(lockInfoVO.getSerialNumber())){
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
        log.error("filePath="+filePath);
        String value = "";
        try (Stream<String> publicLines = Files.lines(Paths.get(filePath),
            StandardCharsets.UTF_8)) {
            value = publicLines.collect(Collectors.joining("\n"));
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

    public static boolean isWin(){
        String osName = System.getProperty("os.name");
        // 判断是否为Windows系统
      return osName.toLowerCase().contains("windows");
    }
}
