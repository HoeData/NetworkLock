package com.ruoyi.license.utils;


import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RsaUtils {

    public static String publicKey =
        "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCxrGSr56hzbSRbo/agsDW5QvnV\n"
            + "SBNNdLxjYMvP2rcxSl1RGByW2HlNuE+RwN2hIhTJHJ47UiHE8ymWO+l9pOjNh1lL\n"
            + "dWAUI+zsDt40ooXbAfAK6zbPxQKrqqFPj8bsBuQ7y9mIh7STbdTcnsid73z9+F/N\n"
            + "+dNO9V+x6qdSlGeKIQIDAQAB";

    public static void testOrTemporary() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("batchNo", "一批次");
        resultMap.put("lockNumber", 5000);
        List<Map<String, Object>> list = new ArrayList<>();
//        for (int i = 0; i <= 9; i++) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("type", 1);
//            map.put("serialNumber", "qwertyuiopasdfg" + i);
//            list.add(map);
//        }
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
        map6.put("type", 1);
        map6.put("serialNumber", "XYZS2408AB000043");
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

        lockList.add("XYZS2408AB000046");
        lockList.add("XYZS2408AB000048");
        lockList.add("XYZS2408AB000049");
        lockList.add("XYZS2408AB000050");
        lockList.add("XYZS2408AB000051");
        lockList.add("XYZS2408AB000052");
        lockList.add("XYZS2408AB000053");
        lockList.add("XYZS2408AB000058");
        lockList.add("XYZS2408AB000059");
        lockList.add("XYZS2408AB000062");

        lockList.forEach(lock -> {
            Map<String, Object> addMap = new HashMap<>();
            addMap.put("type", 2);
            addMap.put("serialNumber", lock);
            list.add(addMap);
        });
        for (int i = 1; i < 200; i++) {
            Map<String, Object> addMap = new HashMap<>();
            addMap.put("type", 2);
            String s = String.valueOf(i);
            int length = s.length();
            if (length == 1) {
                s = "00" + s;
            } else if (length == 2) {
                s = "0" + s;
            }
            addMap.put("serialNumber", "XYZS2526AB000" + s);
            list.add(addMap);
        }
        Map<String, Object> addMap = new HashMap<>();
        addMap.put("type", 2);
        addMap.put("serialNumber", "0XYZ2410AB000000");
        list.add(addMap);
//        for (int i = 1; i < 21; i++) {
//            Map<String, Object> addMap = new HashMap<>();
//            addMap.put("type", 2);
//            String s = String.valueOf(i);
//            int length = s.length();
//            if(length==1){
//                s = "0" + s;
//            }
//            addMap.put("serialNumber", "0XYZ2410AB0000" +s);
//            list.add(addMap);
//        }
        resultMap.put("lockInfoList", list);
        createLicense(resultMap);
    }

    public static void createLicenseForFile(List<String> filePathList, String batchNo,
        Integer lockNumber) {
        List<LockSerialNumber> lockSerialNumberList = new ArrayList<>();
        for (String filePath : filePathList) {
            EasyExcel.read(filePath, LockSerialNumber.class,
                new PageReadListener<LockSerialNumber>(dataList -> {
                    for (LockSerialNumber lockSerialNumber : dataList) {
                        lockSerialNumberList.add(lockSerialNumber);
                    }
                })).sheet().doRead();
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("batchNo", batchNo);
        resultMap.put("lockNumber", lockNumber);
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (LockSerialNumber lockSerialNumber : lockSerialNumberList) {
            Map<String, Object> map = new HashMap<>();
            map.put("type", 1);
            map.put("serialNumber", lockSerialNumber.getSerialNumber());
            mapList.add(map);
        }
        resultMap.put("lockInfoList", mapList);
        createLicense(resultMap);
    }
    public static void createLicense(Map<String, Object> resultMap) {
        RSA rsa = new RSA(null, publicKey);
        String encrypt2 = rsa.encryptBase64(
            StrUtil.bytes(JSON.toJSONString(resultMap), CharsetUtil.CHARSET_UTF_8),
            KeyType.PublicKey);
        try (FileWriter writer = new FileWriter("D:\\out\\lock.license")) {
            writer.write(encrypt2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        List<String> filePath = new ArrayList<>();
        filePath.add("C:\\Users\\xiaomi\\Desktop\\设备信息.xlsx");
        createLicenseForFile(filePath, "沧州", 4000);
    }

}
