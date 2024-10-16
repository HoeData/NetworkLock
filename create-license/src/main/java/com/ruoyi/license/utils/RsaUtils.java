package com.ruoyi.license.utils;


import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.alibaba.fastjson2.JSON;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("batchNo", "123456");
        resultMap.put("lockNumber", 5000);
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i <= 9; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("type", 1);
            map.put("serialNumber", "qwertyuiopasdfg" + i);
            list.add(map);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("type", 1);
        map.put("serialNumber", "XYZS2408AB000036");
        list.add(map);

        Map<String, Object> map1 = new HashMap<>();
        map1.put("type", 1);
        map1.put("serialNumber", "XYZS2408AB000037");
        list.add(map1);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("type", 1);
        map2.put("serialNumber", "XYZS2408AB000038");
        list.add(map2);

        Map<String, Object> map3 = new HashMap<>();
        map3.put("type", 1);
        map3.put("serialNumber", "XYZS2408AB000039");
        list.add(map3);

        Map<String, Object> map4 = new HashMap<>();
        map4.put("type", 1);
        map4.put("serialNumber", "XYZS2408AB000034");
        list.add(map4);

        Map<String, Object> map5 = new HashMap<>();
        map5.put("type", 1);
        map5.put("serialNumber", "XYZS2408AB000041");
        list.add(map5);

        Map<String, Object> map6 = new HashMap<>();
        map5.put("type", 1);
        map5.put("serialNumber", "XYZS2408AB000043");
        list.add(map6);

        Map<String, Object> map7 = new HashMap<>();
        map7.put("type", 1);
        map7.put("serialNumber", "XYZS2408AB000044");
        list.add(map7);

        Set<String> lockList = new HashSet<>();
        lockList.add("XYZS2408AB000025");
        lockList.add("XYZS2408AB000197");
        lockList.add("XYZS2408AB000193");
        lockList.add("XYZS2408AB000026");
        lockList.add("XYZS2408AB000022");
        lockList.add("XYZS2408AB000192");
        lockList.add("XYZS2408AB000181");
        lockList.add("XYZS2408AB000173");
        lockList.add("XYZS2408AB000023");
        lockList.add("XYZS2408AB000178");
        lockList.add("XYZS2408AB000019");
        lockList.add("XYZS2408AB000024");
        lockList.add("XYZS2408AB000183");
        lockList.add("XYZS2408AB000200");
        lockList.add("XYZS2408AB000195");
        lockList.add("XYZS2408AB000004");
        lockList.add("XYZS2408AB000182");
        lockList.add("XYZS2408AB000200");
        lockList.add("XYZS2408AB000195");
        lockList.add("XYZS2408AB000004");
        lockList.add("XYZS2408AB000005");
        lockList.add("XYZS2408AB000006");
        lockList.add("XYZS2408AB000007");
        lockList.add("XYZS2408AB000008");
        lockList.forEach(lock -> {
            Map<String, Object> addMap = new HashMap<>();
            addMap.put("type", 2);
            addMap.put("serialNumber", lock);
            list.add(addMap);
        });
        resultMap.put("lockInfoList", list);
        String encrypt2 = rsa.encryptBase64(
            StrUtil.bytes(JSON.toJSONString(resultMap), CharsetUtil.CHARSET_UTF_8),
            KeyType.PublicKey);
        try (FileWriter writer = new FileWriter("D:\\out\\lock.license")) {
            writer.write(encrypt2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(JSON.toJSONString(rsa.decryptStr(encrypt2, KeyType.PrivateKey)));


    }

}
