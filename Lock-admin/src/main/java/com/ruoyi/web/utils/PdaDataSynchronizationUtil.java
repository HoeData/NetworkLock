package com.ruoyi.web.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.web.constants.LockCache;
import com.ruoyi.web.core.config.LicenseProperties;
import com.ruoyi.web.domain.LockPdaDataSynchronizationInfo;
import com.ruoyi.web.domain.LockPortInfo;
import com.ruoyi.web.domain.LockUnlockLog;
import com.ruoyi.web.domain.RelPdaUserPort;
import com.ruoyi.web.domain.vo.pda.PdaDataSynchronizationStatusVO;
import com.ruoyi.web.domain.vo.pda.PdaDataVO;
import com.ruoyi.web.domain.vo.pda.PdaMergeDataVO;
import com.ruoyi.web.enums.PdaDataSynchronizationStatusType;
import com.ruoyi.web.enums.PdaDataSynchronizationType;
import com.ruoyi.web.service.ILockPdaDataSynchronizationInfoService;
import com.ruoyi.web.service.PdaService;
import com.ruoyi.web.task.InitLicenseRunner;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class PdaDataSynchronizationUtil {

    public static String ADB_PATH = "";
    public static final String PDA_DATA_DIR_PATH = "/sdcard/Android/data/uni.UNI77F4334/documents/";
    private static final String LOCAL_DATA_DIR_PATH = "C:\\dataSynchronization\\";
    private static final String PDA_STATUS_FILENAME = "status.txt";
    private static final String PC_DATA_NAME = "pcData.txt";
    private static final String PDA_DATA_NAME = "pdaData.txt";
    public static final String STATUS_NAME = "status.txt";
    public static PdaDataSynchronizationStatusVO statusVO;
    public static String nowStatusMsg;
    public volatile static boolean RUNNING = false;

    public static void startPdaDataSynchronizationThread() {
        PdaDataSynchronizationStopThread task = new PdaDataSynchronizationStopThread();
        PdaDataSynchronizationStopThread.RUNNING = true;
        Thread thread = new Thread(task);
        thread.start();
    }

    public static void startPdaDataSynchronization() {
        statusVO = new PdaDataSynchronizationStatusVO();
        //获取连接设备ID
        String deviceId = getConnectedDeviceId();
        if (StringUtils.isBlank(deviceId)) {
            PdaDataSynchronizationStopThread.RUNNING = false;
        }
        ILockPdaDataSynchronizationInfoService pdaDataSynchronizationInfoService = SpringUtils.getBean(
            ILockPdaDataSynchronizationInfoService.class);
        LockPdaDataSynchronizationInfo synchronizationInfo = pdaDataSynchronizationInfoService.saveAll(
            deviceId, PdaDataSynchronizationType.AUTHORIZATION_SYNCHRONIZATION);
        statusVO.setReadyFlag(true);
        mkdirFold(LOCAL_DATA_DIR_PATH+deviceId);
        nowStatusMsg = PdaDataSynchronizationStatusType.START.getMsg();
        List<String> pathList = SpringUtils.getBean(LicenseProperties.class).getPathList();
        List<String> fileNameList = new ArrayList<>();
        pathList.forEach(path -> {
            String fileName = getFileName(path);
            fileNameList.add(fileName);
            pushFileToDevice(deviceId, InitLicenseRunner.JAR_PATH + File.separator + "license" + File.separator
                + fileName, PDA_DATA_DIR_PATH + fileName);
        });
        statusVO.setLicensesNameList(fileNameList);
        writeStatusToPda();
        //获取所有同步数据
        PdaService pdaService = SpringUtils.getBean(PdaService.class);
        PdaMergeDataVO pdaMergeDataVO = pdaService.getAllData();
        //等待PDA创建数据文件完成
        setNowStatusMsgAndAddProcess(synchronizationInfo,
            PdaDataSynchronizationStatusType.PDA_CREATE_DATA);
        waitPdaCreateData(deviceId);
        //获取PDA创建文件内容
        setNowStatusMsgAndAddProcess(synchronizationInfo,
            PdaDataSynchronizationStatusType.PC_GET_DATA);
        PdaDataVO fromPdaData = getFromPda(deviceId);
        //创建需要同步给PDA的数据
        setNowStatusMsgAndAddProcess(synchronizationInfo,
            PdaDataSynchronizationStatusType.PC_CREATE_DATA);
//        createGivePdaData(pdaMergeDataVO, fromPdaData, deviceId);
        judgePdaData(pdaMergeDataVO, fromPdaData, synchronizationInfo);
        setNowStatusMsgAndAddProcess(synchronizationInfo,
            PdaDataSynchronizationStatusType.PDA_GET_DATA);
        writeDataToPda(deviceId, pdaMergeDataVO);
        getPdaGetDataFlag(deviceId);
        fromPdaData.setLockPortInfo(pdaMergeDataVO.getPortInfoList());
        pdaService.update(fromPdaData);
        statusVO.setEndFlag(true);
        writeStatusToPda();
        setNowStatusMsgAndAddProcess(synchronizationInfo, PdaDataSynchronizationStatusType.END);
        removePdaFile(deviceId);
        PdaDataSynchronizationStopThread.RUNNING=false;
    }

    private static void waitPdaCreateData(String deviceId) {
        while (!statusVO.getPdaCreateDataFlag()) {
            checkDeviceId(deviceId);
            getPdaStatus(deviceId);
        }
    }

    private static void getPdaStatus(String deviceId) {
        try {
            statusVO = JSON.parseObject(
                readFileContentFromDevice(deviceId, PDA_DATA_DIR_PATH + PDA_STATUS_FILENAME),
                PdaDataSynchronizationStatusVO.class);
            if (statusVO.getErrorFlag()) {
                PdaDataSynchronizationStopThread.RUNNING = false;
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

    private static PdaDataVO getFromPda(String deviceId) {
        PdaDataVO fromPdaData = null;
        int error = 0;
        while (true) {
            try {
                if (error > 10) {
                    PdaDataSynchronizationStopThread.RUNNING = false;
                    statusVO.setErrorFlag(true);
                    writeStatusToPda();
                    break;
                }
                fromPdaData = JSON.parseObject(
                    readFileContentFromDevice(deviceId, PDA_DATA_DIR_PATH + PDA_DATA_NAME)                    ,
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
            PdaDataSynchronizationStopThread.RUNNING = false;
            throw new IOException("Failed to read file from device with exit code: " + exitCode);
        }
        return fileContent.toString();
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
            log.info("Push command executed with exit code: " + exitCode);
            if (exitCode != 0) {
                PdaDataSynchronizationStopThread.RUNNING = false;
                throw new IOException(
                    "Failed to read file from device with exit code: " + exitCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            PdaDataSynchronizationStopThread.RUNNING = false;
            log.error("Failed to push file to the device.");
        }
    }

    private static void judgePdaData(PdaMergeDataVO pdaMergeDataVO,
        PdaDataVO fromPdaData, LockPdaDataSynchronizationInfo synchronizationInfo) {
        List<LockPortInfo> pcPortList = pdaMergeDataVO.getPortInfoList();
        List<LockUnlockLog> lockUnlockLogList;
        Map<Integer, LockPortInfo> pdaPortMap = new HashMap<>();
        if (null != fromPdaData) {
            List<LockPortInfo> pdaPortList = fromPdaData.getLockPortInfo();
            for(LockPortInfo lockPortInfo:pdaPortList){
                pdaPortMap.put(lockPortInfo.getId(), lockPortInfo);
            }
            lockUnlockLogList= fromPdaData.getLockUnlockLog();
            pdaMergeDataVO.getUnlockLogList().addAll(lockUnlockLogList);
        }

        int value = 0;
        List<LockPortInfo> list=new ArrayList<>();
        for (LockPortInfo lockPortInfo : pcPortList) {
            if(pdaPortMap.containsKey(lockPortInfo.getId())){
                list.add(pdaPortMap.get(lockPortInfo.getId()));
            }else{
                list.add(lockPortInfo);
            }
            if (StringUtils.isNotBlank(lockPortInfo.getUserCode())) {
                value++;
            }
        }
        if (value > LockCache.lockNumber) {
            statusVO.setErrorFlag(true);
            writeStatusToPda();
            setNowStatusMsgAndAddProcess(synchronizationInfo,
                PdaDataSynchronizationStatusType.MAXIMUM_NUMBER_EXCEEDED);
            PdaDataSynchronizationStopThread.RUNNING = false;

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

    private static void getEndFlag(String deviceId) {
        while (!statusVO.getEndFlag()) {
            getPdaStatus(deviceId);
        }
    }

    private static void checkDeviceId(String deviceId) {
        if (!StringUtils.equals(getConnectedDeviceId(), deviceId)) {
            throw new ServiceException("设备已断开连接");
        }
    }

    public static void writeStringToFile(String content, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
            PdaDataSynchronizationStopThread.RUNNING = false;
        }
    }

    private static void setNowStatusMsgAndAddProcess(
        LockPdaDataSynchronizationInfo synchronizationInfo,
        PdaDataSynchronizationStatusType statusType) {
        ILockPdaDataSynchronizationInfoService pdaDataSynchronizationInfoService = SpringUtils.getBean(
            ILockPdaDataSynchronizationInfoService.class);
        nowStatusMsg = statusType.getMsg();
        pdaDataSynchronizationInfoService.updateStatus(synchronizationInfo, statusType.getValue());
    }


    public static void startPdaAuthorization(List<RelPdaUserPort> list) {
        PdaDataSynchronizationUtil.refreshAll();
        statusVO = new PdaDataSynchronizationStatusVO();
        //获取连接设备ID
        String deviceId = getConnectedDeviceId();
        if (StringUtils.isBlank(deviceId)) {
            throw new ServiceException("PDA设备未连接");
        }
        ILockPdaDataSynchronizationInfoService pdaDataSynchronizationInfoService = SpringUtils.getBean(
            ILockPdaDataSynchronizationInfoService.class);
        LockPdaDataSynchronizationInfo synchronizationInfo = pdaDataSynchronizationInfoService.saveAll(
            deviceId, PdaDataSynchronizationType.DATA_SYNCHRONIZATION);
        statusVO.setReadyFlag(true);
        writeStringToFile(JSON.toJSONString(statusVO),
            LOCAL_DATA_DIR_PATH + deviceId + File.separator + STATUS_NAME);
        pushFileToDevice(deviceId, LOCAL_DATA_DIR_PATH + deviceId + File.separator + STATUS_NAME,
            PDA_DATA_DIR_PATH + STATUS_NAME);
        //获取所有同步数据
        PdaMergeDataVO pdaMergeDataVO = new PdaMergeDataVO();
        pdaMergeDataVO.setRelPdaUserPortList(list);
        //创建需要同步给PDA的数据
        setNowStatusMsgAndAddProcess(synchronizationInfo,
            PdaDataSynchronizationStatusType.PDA_GET_DATA);
        writeDataToPda(deviceId, pdaMergeDataVO);
        getPdaGetDataFlag(deviceId);
        getEndFlag(deviceId);
        setNowStatusMsgAndAddProcess(synchronizationInfo, PdaDataSynchronizationStatusType.END);
        PdaDataSynchronizationStopThread.RUNNING = false;
    }

    public static void writeStatusToPda() {
        String deviceId = getConnectedDeviceId();
        writeStringToFile(JSON.toJSONString(statusVO),
            LOCAL_DATA_DIR_PATH + deviceId + File.separator + STATUS_NAME);
        pushFileToDevice(deviceId, LOCAL_DATA_DIR_PATH + deviceId + File.separator + STATUS_NAME,
            PDA_DATA_DIR_PATH + STATUS_NAME);
    }

    public static void refreshAll() {
        PdaDataSynchronizationUtil.RUNNING = false;
        PdaDataSynchronizationStopThread.RUNNING=false;
        PdaDataSynchronizationUtil.statusVO = new PdaDataSynchronizationStatusVO();
        PdaDataSynchronizationUtil.nowStatusMsg = "未同步";
    }

    public static String getFileName(String filePath) {
        // 查找最后一个路径分隔符的位置
        int lastSeparatorIndex = filePath.lastIndexOf(File.separator);
        // 如果没有找到路径分隔符，则整个字符串就是文件名
        if (lastSeparatorIndex == -1) {
            lastSeparatorIndex = 0;
        } else {
            lastSeparatorIndex++;  // 跳过分隔符
        }
        // 提取文件名
        String fileName = filePath.substring(lastSeparatorIndex);
        return fileName;
    }

    public static void removePdaFile(String deviceId) {
        try {
            Process process = Runtime.getRuntime().exec(
                ADB_PATH + " -s " + deviceId + " shell rm -rf " + PDA_DATA_DIR_PATH + "*");
            int exitCode = process.waitFor();
            log.info("remove command executed with exit code: " + exitCode);
            if (exitCode != 0) {
                PdaDataSynchronizationStopThread.RUNNING = false;
                throw new IOException(
                    "remove to read file from device with exit code: " + exitCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            PdaDataSynchronizationStopThread.RUNNING = false;
            log.error("remove to push file to the device.");
        }
    }
    private static void mkdirFold(String folderPath){
        File folder = new File(folderPath);
        // 检查文件夹是否存在
        if (!folder.exists()) {
            // 文件夹不存在，尝试创建它
            boolean wasSuccessful = folder.mkdirs();
            if (wasSuccessful) {
                log.info("文件夹已成功创建: " + folder.getAbsolutePath());
            } else {
                log.error("无法创建文件夹: " + folder.getAbsolutePath());
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(getConnectedDeviceId());
    }
}
