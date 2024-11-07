package com.manniu.offline.controller;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Maps;
import com.manniu.offline.domain.LockPortInfo;
import com.manniu.offline.domain.LockUnlockLog;
import com.manniu.offline.domain.PdaDataSynchronizationStatusType;
import com.manniu.offline.domain.PdaDataSynchronizationStatusVO;
import com.manniu.offline.domain.PdaDataVO;
import com.manniu.offline.domain.PdaMergeDataVO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/lock/pdaOfflineSynchronization")
public class PdaOfflineSynchronizationController {

    public static String ADB_PATH = "";
    public static final String PDA_DATA_DIR_PATH = "/sdcard/Android/data/uni.UNI77F4334/documents/";
    private static final String LOCAL_DATA_DIR_PATH = "C:\\dataSynchronization\\";
    private static final String PDA_STATUS_FILENAME = "status.txt";
    private static final String PC_DATA_NAME = "pcData.txt";
    private static final String PDA_DATA_NAME = "pdaData.txt";
    public static final String STATUS_NAME = "status.txt";
    public static PdaDataSynchronizationStatusVO statusVO;
    public static String nowStatusMsg;
    public static final String PDA_EXPORT_PRIVATE_KEY =
        "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALTFazs4qOsLDsqG\n"
            + "jlpDo3dERvJoUngltlOcPNhswy0HTQ2EJdtI4l2HKZaJsHZaam7sz2tW6gStIdhx\n"
            + "YpBc4UlmdsmshAhPe33Z3ArTD/uZgPbjAJnYpyweFYlk7K5AIa62c2LNoW6q95ax\n"
            + "dwmbCAqIRG1QmvW/4VUfu7y3xYvTAgMBAAECgYAMxsdFcEPAGQ/6kHgPOSicjV7W\n"
            + "UzlA9KlmT2ydf1JsJE/13JkwXn5hTeRKl4C5XLqZDHO8inAP1IzH13u36Fij0P0q\n"
            + "WksjmaWl2wV6cNdO3i+3HuvfB3v8UKSUIIeIMIpq0q6LrVZk+fO60dcP6Kjrada9\n"
            + "pTGfBRU2AzVqkCZUAQJBAOOJEIp1vPWX2mslPc9DE2jW0E5HrUHFarSQmZsPA2p9\n"
            + "+nIwrAdi7zLcDBfYNgGXaxPscCeYOY/maF/m7yxRc8ECQQDLYrS91AxNcLWF9pwH\n"
            + "rtuBp6WNCmQ5bGDE/kMSLMVb6Aza9IusCxTk1dyH1KvSbi/h4DJxHsyeSCDmG6sx\n"
            + "NRSTAkAchz3srlBv1odLMdMrHnTbizt45SHDAlabpxmrSFmcS4lQMewPzQbCsLZP\n"
            + "cwtqbaq+R8HUJRDqivABPjo0q03BAkBKB+jAPCIqQf9g/s32ofg2bn59Iy4uFLv4\n"
            + "mJBzWiaQeJvNSzxX6ES3svyt2ISeeQsmzcOul0Zlyt1mxOWAaNDPAkEA2vKdKGKa\n"
            + "IIlsov/7x+dl/4c32S79V7SWMscbg2HXO7PCoDgZviEvWV2wQ2OsYIsT5c+2s56s\n"
            + "G5698YMwYLHHxw==";
    public static final String LICENSE_PRIVATE_KEY =
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

    @GetMapping("/start")
    public Map<String, Object> error(@RequestParam("file") MultipartFile file) {
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("code", 200);
        resultMap.put("msg", "同步成功");
        try {
            String content = new BufferedReader(
                new InputStreamReader(file.getInputStream())).lines()
                .collect(Collectors.joining("\n"));
            RSA rsa = new RSA(PDA_EXPORT_PRIVATE_KEY, null);
            Map<String, Object> map = JSON.parseObject(rsa.decryptStr(content, KeyType.PrivateKey),
                Map.class);
            if (!StringUtils.equals(getConnectedDeviceId(), map.get("pdaKey").toString())) {
                throw new RuntimeException("PDA不匹配");
            }
            PdaMergeDataVO pdaMergeDataVO = (PdaMergeDataVO) map.get("pdaMergeDataVO");
            List<String> licenseStrList = (List<String>) map.get("licenseStrList");

        } catch (Exception e) {
            resultMap.put("code", "500");
            resultMap.put("msg", "同步失败,请重试");
        }
        //TODO 开始同步代码
        return resultMap;
    }

    public static void pdaOfflineDataSynchronization(PdaMergeDataVO pdaMergeDataVO,
        List<String> licenseStrList) {
        statusVO = new PdaDataSynchronizationStatusVO();
        //获取连接设备ID
        String deviceId = getConnectedDeviceId();
        if (StringUtils.isBlank(deviceId)) {
            throw new RuntimeException("未检测到连接设备,请确认");
        }
        statusVO.setReadyFlag(true);
        mkdirFold(LOCAL_DATA_DIR_PATH + deviceId);
        nowStatusMsg = PdaDataSynchronizationStatusType.START.getMsg();
        //TODO 重写生成license文件地址 打包exe以后再看是否可以获取到license地址，如果可以就不重写，如果不可以，就需要重写
//        List<String> pathList = SpringUtils.getBean(LicenseProperties.class).getPathList();
//        List<String> fileNameList = new ArrayList<>();
//        pathList.forEach(path -> {
//            String fileName = getFileName(path);
//            fileNameList.add(fileName);
//            pushFileToDevice(deviceId, InitLicenseRunner.JAR_PATH + File.separator + "license" + File.separator
//                + fileName, PDA_DATA_DIR_PATH + fileName);
//        });
//        statusVO.setLicensesNameList(fileNameList);
        writeStatusToPda();
        //等待PDA创建数据文件完成
        waitPdaCreateData(deviceId);
        nowStatusMsg = PdaDataSynchronizationStatusType.PDA_CREATE_DATA.getMsg();
        //获取PDA创建文件内容
        nowStatusMsg = PdaDataSynchronizationStatusType.PC_GET_DATA.getMsg();
        PdaDataVO fromPdaData = getFromPda(deviceId);
        //创建需要同步给PDA的数据
        nowStatusMsg = PdaDataSynchronizationStatusType.PC_CREATE_DATA.getMsg();
        judgePdaData(pdaMergeDataVO, fromPdaData);
        nowStatusMsg = PdaDataSynchronizationStatusType.PDA_GET_DATA.getMsg();
        writeDataToPda(deviceId, pdaMergeDataVO);
        getPdaGetDataFlag(deviceId);
        fromPdaData.setLockPortInfo(pdaMergeDataVO.getPortInfoList());
        statusVO.setEndFlag(true);
        writeStatusToPda();
        nowStatusMsg = PdaDataSynchronizationStatusType.END.getMsg();
        removePdaFile(deviceId);

    }

    public static String getConnectedDeviceId() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(ADB_PATH + " devices");
            InputStream in = process.getInputStream();
            try (BufferedReader read = new BufferedReader(new InputStreamReader(in))) {
                String str;
                boolean firstLine = true;
                while ((str = read.readLine()) != null) {
                    if (firstLine) {
                        firstLine = false; // 跳过第一行
                        continue;
                    }
                    if (!str.trim().isEmpty()) { // 跳过空行
                        String[] parts = str.split("\\s+");
                        return parts[0]; // 返回第一个设备ID
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return null;
    }

    private static void mkdirFold(String folderPath) {
        File folder = new File(folderPath);
        // 检查文件夹是否存在
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    public static void writeStatusToPda() {
        String deviceId = getConnectedDeviceId();
        writeStringToFile(JSON.toJSONString(statusVO),
            LOCAL_DATA_DIR_PATH + deviceId + File.separator + STATUS_NAME);
        pushFileToDevice(deviceId, LOCAL_DATA_DIR_PATH + deviceId + File.separator + STATUS_NAME,
            PDA_DATA_DIR_PATH + STATUS_NAME);
    }

    public static void writeStringToFile(String content, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("同步失败");
        }
    }

    /**
     * 向指定设备推送文件。
     *
     * @param deviceId       设备ID
     * @param localFilePath  本地文件路径
     * @param remoteFilePath 设备上的目标路径
     */
    private static void pushFileToDevice(String deviceId, String localFilePath,
        String remoteFilePath) {
        try {
            Process process = Runtime.getRuntime().exec(
                ADB_PATH + " -s " + deviceId + " push " + localFilePath + " " + remoteFilePath);
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("同步失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("同步失败");
        }
    }

    private static void waitPdaCreateData(String deviceId) {
        while (!statusVO.getPdaCreateDataFlag()) {
            checkDeviceId(deviceId);
            getPdaStatus(deviceId);
        }
    }

    private static void checkDeviceId(String deviceId) {
        if (!StringUtils.equals(getConnectedDeviceId(), deviceId)) {
            throw new RuntimeException("设备已断开连接");
        }
    }

    private static void getPdaStatus(String deviceId) {
        try {
            statusVO = JSON.parseObject(
                readFileContentFromDevice(deviceId, PDA_DATA_DIR_PATH + PDA_STATUS_FILENAME),
                PdaDataSynchronizationStatusVO.class);
            if (statusVO.getErrorFlag()) {
                throw new RuntimeException("同步失败");
            }
        } catch (Exception e) {

        } finally {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 从设备读取文件内容
     *
     * @param deviceId       设备id
     * @param remoteFilePath 远程文件路径
     * @return {@link String }
     * @throws Exception 例外
     */
    public static String readFileContentFromDevice(String deviceId, String remoteFilePath)
        throws Exception {
        StringBuilder fileContent = new StringBuilder();
        Process process = Runtime.getRuntime()
            .exec(ADB_PATH + " -s " + deviceId + " shell cat " + remoteFilePath);
        try (InputStreamReader inputStreamReader = new InputStreamReader(
            process.getInputStream()); BufferedReader reader = new BufferedReader(
            inputStreamReader)) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append(System.lineSeparator());
            }
        }
        int exitCode = 0;
        exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("同步失败");
        }
        return fileContent.toString();
    }

    private static PdaDataVO getFromPda(String deviceId) {
        PdaDataVO fromPdaData = null;
        int error = 0;
        while (true) {
            try {
                if (error > 10) {
                    statusVO.setErrorFlag(true);
                    writeStatusToPda();
                    throw new RuntimeException("同步失败");
                }
                fromPdaData = JSON.parseObject(
                    readFileContentFromDevice(deviceId, PDA_DATA_DIR_PATH + PDA_DATA_NAME),
                    PdaDataVO.class);
                break;
            } catch (Exception e) {
                error++;
            } finally {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return fromPdaData;
    }

    private static void judgePdaData(PdaMergeDataVO pdaMergeDataVO, PdaDataVO fromPdaData) {
        List<LockPortInfo> pcPortList = pdaMergeDataVO.getPortInfoList();
        List<LockUnlockLog> lockUnlockLogList;
        Map<Integer, LockPortInfo> pdaPortMap = new HashMap<>();
        if (null != fromPdaData) {
            List<LockPortInfo> pdaPortList = fromPdaData.getLockPortInfo();
            for (LockPortInfo lockPortInfo : pdaPortList) {
                pdaPortMap.put(lockPortInfo.getId(), lockPortInfo);
            }
            lockUnlockLogList = fromPdaData.getLockUnlockLog();
            pdaMergeDataVO.getUnlockLogList().addAll(lockUnlockLogList);
        }

        int value = 0;
        List<LockPortInfo> list = new ArrayList<>();
        for (LockPortInfo lockPortInfo : pcPortList) {
            if (pdaPortMap.containsKey(lockPortInfo.getId())) {
                list.add(pdaPortMap.get(lockPortInfo.getId()));
            } else {
                list.add(lockPortInfo);
            }
            if (StringUtils.isNotBlank(lockPortInfo.getUserCode())) {
                value++;
            }
        }
        pdaMergeDataVO.setPortInfoList(list);
    }

    private static void writeDataToPda(String deviceId, PdaMergeDataVO givePdaData) {
        writeStringToFile(JSON.toJSONString(givePdaData),
            LOCAL_DATA_DIR_PATH + deviceId + File.separator + PC_DATA_NAME);
        pushFileToDevice(deviceId, LOCAL_DATA_DIR_PATH + deviceId + File.separator + PC_DATA_NAME,
            PDA_DATA_DIR_PATH + PC_DATA_NAME);
        statusVO.setPcCreateDataFlag(true);
        writeStringToFile(JSON.toJSONString(statusVO),
            LOCAL_DATA_DIR_PATH + deviceId + File.separator + PDA_STATUS_FILENAME);
        pushFileToDevice(deviceId,
            LOCAL_DATA_DIR_PATH + deviceId + File.separator + PDA_STATUS_FILENAME,
            PDA_DATA_DIR_PATH + PDA_STATUS_FILENAME);
        getPdaStatus(deviceId);
    }

    private static void getPdaGetDataFlag(String deviceId) {
        while (!statusVO.getPdaGetDataFlag()) {
            checkDeviceId(deviceId);
            getPdaStatus(deviceId);
        }
    }

    public static void removePdaFile(String deviceId) {
        try {
            Process process = Runtime.getRuntime()
                .exec(ADB_PATH + " -s " + deviceId + " shell rm -rf " + PDA_DATA_DIR_PATH + "*");
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException(
                    "remove to read file from device with exit code: " + exitCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
