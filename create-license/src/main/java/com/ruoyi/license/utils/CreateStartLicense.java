package com.ruoyi.license.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.alibaba.fastjson2.JSON;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class CreateStartLicense {
    public static void main(String[] args) {
        StartLicenseVO startLicenseVO = new StartLicenseVO();
        startLicenseVO.setProductId(
            "917385a1376911ec1a4e246c8765e41880:b6:55:4f:d3:fa80:b6:55:4f:d3:fe02:50:f2:00:00:02");
        startLicenseVO.setEndTime(LocalDateTime.now().plusDays(30 * 12 * 30));
        startLicenseVO.setTemporaryAuthorization(false);
        System.out.println(JSON.toJSONString(startLicenseVO));
        String fileName = "D:\\out\\start.license";
        RSA rsa = new RSA(null, RsaUtils.publicKey);
        String encrypt2 = rsa.encryptBase64(
            StrUtil.bytes(JSON.toJSONString(startLicenseVO), CharsetUtil.CHARSET_UTF_8),
            KeyType.PublicKey);
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(encrypt2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
