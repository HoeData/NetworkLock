package com.ruoyi.license.utils;


import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import javax.crypto.Cipher;

public class RsaUtils {


    public static void main(String[] args) throws Exception {
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCxrGSr56hzbSRbo/agsDW5QvnV\n"
            + "SBNNdLxjYMvP2rcxSl1RGByW2HlNuE+RwN2hIhTJHJ47UiHE8ymWO+l9pOjNh1lL\n"
            + "dWAUI+zsDt40ooXbAfAK6zbPxQKrqqFPj8bsBuQ7y9mIh7STbdTcnsid73z9+F/N\n"
            + "+dNO9V+x6qdSlGeKIQIDAQAB";
        String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALGsZKvnqHNtJFuj\n"
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
        RSA rsa = new RSA(privateKey, publicKey);
        Map<String, Object> map = new HashMap<>();
        map.put("batchNo", "123456");
        map.put("lockNumber", 5000);
        List<String> aa = new ArrayList<>();
        for (int i = 0; i <= 1; i++) {
            aa.add("1234567890123456");
        }
        map.put("lockSerialNumberList", aa);
        String encrypt2 = rsa.encryptBase64(
            StrUtil.bytes(JSON.toJSONString(map), CharsetUtil.CHARSET_UTF_8), KeyType.PrivateKey);
        System.out.println(encrypt2);


        byte[] keyBytes = Base64.decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey1 = keyFactory.generatePublic(keySpec);


        // 生成一个密钥对
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, publicKey1);
        byte[] encryptedBytes = Base64.decode(encrypt2);

        // 定义最大解密块大小
        int blockSize = 128; // 1024位RSA密钥的最大块大小，减去一些开销

        StringBuilder decryptedText = new StringBuilder();

        // 分割数据并逐个解密
        for (int i = 0; i < encryptedBytes.length; i += blockSize) {
            byte[] block = new byte[Math.min(blockSize, encryptedBytes.length - i)];
            System.arraycopy(encryptedBytes, i, block, 0, block.length);
            // 执行解密操作
            byte[] decryptedBlock = cipher.doFinal(block);
            // 将解密后的字节数组转换为字符串并拼接
            decryptedText.append(new String(decryptedBlock, "UTF-8"));
        }
        System.out.println(decryptedText);

        try (FileWriter writer = new FileWriter("D:\\out\\lock.license")) {
            writer.write(encrypt2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long a = System.currentTimeMillis();
        System.out.println("cost=" + (System.currentTimeMillis() - a));
        List<String> valueList = new ArrayList<>();
        valueList.add("1");
        valueList.add("2");
        valueList.add("3");
        valueList.add("4");
        valueList.add("5");
        final CountDownLatch latch = new CountDownLatch(valueList.size());
        JSONObject result = new JSONObject();
        final JSONObject data = new JSONObject();
        final List<String> lockSerialNumberList = new ArrayList<>();
        for (String value : valueList) {
            final String val = value;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    lockSerialNumberList.add(val);
                    // 计数器减一，通知等待线程
                    latch.countDown();
                }
            }).start();
        }
        try {
            // 主线程等待数据聚合完成
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        result.put("lockSerialNumberList", lockSerialNumberList);
        System.out.println(JSON.toJSONString(result));


    }

    private static void getPublicKey() {
        String content = "测试一下AES加密";
//随机生成密钥
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();

//构建
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);

//加密
        byte[] encrypt = aes.encrypt(content);
//解密
        byte[] decrypt = aes.decrypt(encrypt);

//加密为16进制表示
        String encryptHex = aes.encryptHex(content);
//解密为字符串
        String decryptStr = aes.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);
    }

    public static String decryptPrivate(String privateKey, String str) {
        RSA rsa = new RSA(privateKey, null);
        return rsa.decryptStr(str, KeyType.PrivateKey);
    }
}
