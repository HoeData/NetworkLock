package com.manniu.offline.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.http.HttpUtil;
import cn.hutool.setting.Setting;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.manniu.offline.domain.LockPdaDataSynchronizationInfo;
import com.manniu.offline.domain.LockPortInfo;
import com.manniu.offline.domain.LockUnlockLog;
import com.manniu.offline.domain.PdaDataSynchronizationStatusType;
import com.manniu.offline.domain.PdaDataSynchronizationStatusVO;
import com.manniu.offline.domain.PdaDataVO;
import com.manniu.offline.domain.PdaMergeDataVO;
import com.manniu.offline.domain.SynchronizationVO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/lock/pdaOfflineSynchronization")
public class PdaOfflineSynchronizationController {


//    public static String ADB_PATH = "D:\\out\\platform-tools-latest-windows\\platform-tools\\adb.exe";
    public static String ADB_PATH = "";
    public static final String PDA_DATA_DIR_PATH = "/sdcard/Android/data/uni.UNI77F4334/documents/";
    private static final String LOCAL_DATA_DIR_PATH = "C:\\dataSynchronization\\";
    private static final String PDA_STATUS_FILENAME = "status.txt";
    private static final String PC_DATA_NAME = "pcData.txt";
    private static final String PDA_DATA_NAME = "pdaData.txt";
    public static final String STATUS_NAME = "status.txt";
    public static PdaDataSynchronizationStatusVO statusVO;
    public static String nowStatusMsg;
    public static final String AES_KEY = "uuNbz89psCnbtJlm";
    private static String SERVER_URL = "";
    private static boolean serverConnectFlag = false;


    @PostMapping("/start")
    public Object start(MultipartFile file) {
        SynchronizationVO synchronizationVO;
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("code", 200);
        resultMap.put("msg", "同步成功");
        try {
            ADB_PATH = System.getProperty("user.dir") + File.separator + "config" + File.separator
                + "platform-tools" + File.separator + "adb.exe";
            String deviceId = getConnectedDeviceId();
            if (StringUtils.isBlank(deviceId)) {
                throw new RuntimeException("未识别到PDA");
            }
            Setting setting = new Setting(FileUtil.touch(
                System.getProperty("user.dir") + File.separator + "config" + File.separator
                    + "config.setting"), CharsetUtil.CHARSET_UTF_8, true);
            SERVER_URL = setting.getStr("serverUrl", "");
            synchronizationVO = getDataForApi(deviceId);
            if (synchronizationVO == null && (null == file || StringUtils.isBlank(
                file.getOriginalFilename()))) {
                throw new RuntimeException("与服务端连接超时,请上传同步文件进行同步");
            }
            if (null == synchronizationVO) {
                synchronizationVO = getDataForFile(file);
            }
            if (null == synchronizationVO) {
                throw new RuntimeException("同步文件检测失败无法同步");
            }
            if (!StringUtils.equals(deviceId, synchronizationVO.getDeviceId())) {
                throw new RuntimeException("PDA不匹配" + deviceId);
            }
            pdaOfflineDataSynchronization(synchronizationVO);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("code", "500");
            resultMap.put("msg", e.getMessage());
        }
        return resultMap;
    }

    @PostMapping("/exportPdaData")
    public Object exportPdaData() {
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("code", 200);
        try {
            String deviceId = getConnectedDeviceId();
            if (StringUtils.isBlank(deviceId)) {
                throw new RuntimeException("未识别到PDA");
            }
            PdaDataVO pdaDataVO = getFromPda(deviceId);
            if (null == pdaDataVO) {
                throw new RuntimeException("PDA数据获取失败,请检查后重试");
            }
            AES aes = SecureUtil.aes(AES_KEY.getBytes());
            String encryptHex = aes.encryptHex(JSON.toJSONString(pdaDataVO));
            writeStringToFile(encryptHex,
                LOCAL_DATA_DIR_PATH + deviceId + File.separator + "pdaData.cer");
            resultMap.put("msg",
                "导出PDA数据成功,文件已存放在" + LOCAL_DATA_DIR_PATH + deviceId + File.separator
                    + "pdaData.cer");
            return resultMap;
        } catch (Exception e) {
            resultMap.put("code", "500");
            resultMap.put("msg", e.getMessage());
        }

        return resultMap;
    }

    private SynchronizationVO getDataForApi(String deviceId) {
        SynchronizationVO synchronizationVO = null;
        try {
            String result = HttpUtil.get(SERVER_URL + "/pda/dataSynchronization/getStatus", 5000);
            if ("200".equals(JSON.parseObject(result).get("code").toString())) {
                String data = HttpUtil.get(
                    SERVER_URL + "/pda/dataSynchronization/getAllData/" + deviceId,
                    1000*120);
                synchronizationVO = JSON.parseObject(data, SynchronizationVO.class);
                serverConnectFlag = true;
            }
            return synchronizationVO;
        } catch (Exception e) {
            return null;
        }

    }

    @SneakyThrows
    private SynchronizationVO getDataForFile(MultipartFile file) {
        try {
            String content = new BufferedReader(
                new InputStreamReader(file.getInputStream())).lines()
                .collect(Collectors.joining("\n"));
            AES aes = SecureUtil.aes(AES_KEY.getBytes());
            return JSON.parseObject(aes.decryptStr(content), SynchronizationVO.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static void pdaOfflineDataSynchronization(SynchronizationVO synchronizationVO) {
        statusVO = new PdaDataSynchronizationStatusVO();
        //获取连接设备ID
        String deviceId = getConnectedDeviceId();
        if (StringUtils.isBlank(deviceId)) {
            throw new RuntimeException("未检测到连接设备,请确认");
        }
        mkdirFold(LOCAL_DATA_DIR_PATH + deviceId);
        statusVO.setReadyFlag(true);
        List<String> licenseStrList = synchronizationVO.getLicenseStrList();
        List<String> fileNameList = Lists.newArrayList();
        int lockSize = synchronizationVO.getLicenseMaxNumber();
        for (int i = 0; i < licenseStrList.size(); i++) {
            String str = licenseStrList.get(i);
            String fileName = "lock" + i + ".license";
            fileNameList.add(fileName);
            String path = LOCAL_DATA_DIR_PATH + deviceId + File.separator + fileName;
            writeStringToFile(str, path);
            pushFileToDevice(deviceId, path, PDA_DATA_DIR_PATH + fileName);
        }
        statusVO.setLicensesNameList(fileNameList);
        LockPdaDataSynchronizationInfo synchronizationInfo = setSynchronizationInfo(deviceId);
        setNowStatusMsgAndAddProcess(synchronizationInfo, PdaDataSynchronizationStatusType.START);
        writeStatusToPda();
        //等待PDA创建数据文件完成
        waitPdaCreateData(deviceId);
        setNowStatusMsgAndAddProcess(synchronizationInfo,
            PdaDataSynchronizationStatusType.PDA_CREATE_DATA);
        //获取PDA创建文件内容
        setNowStatusMsgAndAddProcess(synchronizationInfo,
            PdaDataSynchronizationStatusType.PC_GET_DATA);
        PdaDataVO fromPdaData = getFromPda(deviceId);
        //创建需要同步给PDA的数据
        setNowStatusMsgAndAddProcess(synchronizationInfo,
            PdaDataSynchronizationStatusType.PC_CREATE_DATA);
        judgePdaData(synchronizationVO.getPdaMergeDataVO(), fromPdaData, lockSize,
            synchronizationInfo);
        setNowStatusMsgAndAddProcess(synchronizationInfo,
            PdaDataSynchronizationStatusType.PDA_GET_DATA);
        writeDataToPda(deviceId, synchronizationVO.getPdaMergeDataVO());
        getPdaGetDataFlag(deviceId);
        fromPdaData.setLockPortInfo(synchronizationVO.getPdaMergeDataVO().getPortInfoList());
        update(fromPdaData);
        statusVO.setEndFlag(true);
        writeStatusToPda();
        setNowStatusMsgAndAddProcess(synchronizationInfo, PdaDataSynchronizationStatusType.END);
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

    private static void judgePdaData(PdaMergeDataVO pdaMergeDataVO, PdaDataVO fromPdaData,
        int lockSize, LockPdaDataSynchronizationInfo synchronizationInfo) {
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
        Set<String> set = Sets.newHashSet();
        List<LockPortInfo> list = Lists.newArrayList();
        for (LockPortInfo lockPortInfo : pcPortList) {
            if (pdaPortMap.containsKey(lockPortInfo.getId())
                && pdaPortMap.get(lockPortInfo.getId()).getUpdateTime().getTime()
                >= lockPortInfo.getUpdateTime().getTime()) {
                list.add(pdaPortMap.get(lockPortInfo.getId()));
            } else {
                list.add(lockPortInfo);
            }
            if (StringUtils.isNotBlank(lockPortInfo.getUserCode())) {
                set.add(lockPortInfo.getUserCode());
            }
        }
        if (value > lockSize) {
            statusVO.setErrorFlag(true);
            writeStatusToPda();
            setNowStatusMsgAndAddProcess(synchronizationInfo,
                PdaDataSynchronizationStatusType.MAXIMUM_NUMBER_EXCEEDED);
            throw new RuntimeException(
                PdaDataSynchronizationStatusType.MAXIMUM_NUMBER_EXCEEDED.getMsg());
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

    private static LockPdaDataSynchronizationInfo setSynchronizationInfo(String deviceId) {
        if (serverConnectFlag) {
            try {
                LockPdaDataSynchronizationInfo synchronizationInfo = JSONObject.parseObject(
                    JSON.toJSONString(JSONObject.parseObject(HttpUtil.get(
                            SERVER_URL + "/pda/dataSynchronization/saveAll/" + deviceId + "/2", 5000),
                        Map.class).get("data")), LockPdaDataSynchronizationInfo.class);
                return synchronizationInfo;
            } catch (Exception e) {
                return null;

            }
        }
        return null;
    }

    private static void setNowStatusMsgAndAddProcess(
        LockPdaDataSynchronizationInfo synchronizationInfo,
        PdaDataSynchronizationStatusType statusType) {
        if (null != synchronizationInfo) {
            try {
                synchronizationInfo.setStatus(statusType.getValue());
                HttpUtil.createPost(SERVER_URL + "/pda/dataSynchronization/update")
                    .contentType("application/json").body(JSON.toJSONString(synchronizationInfo))
                    .timeout(1000*10).execute().body();

            } catch (Exception e) {
            }
        }
        nowStatusMsg = statusType.getMsg();
    }

    private static void update(PdaDataVO fromPdaData) {
        if (serverConnectFlag) {
            try {
                HttpUtil.createPost(SERVER_URL + "/pda/dataSynchronization/updatePdaData")
                    .contentType("application/json").body(JSON.toJSONString(fromPdaData))
                    .timeout(1000*60).execute().body();
            } catch (Exception e) {
            }
        }
    }

}
