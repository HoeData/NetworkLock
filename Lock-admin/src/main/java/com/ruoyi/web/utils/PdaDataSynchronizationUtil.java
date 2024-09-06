package com.ruoyi.web.utils;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.web.domain.LockPdaDataSynchronizationInfo;
import com.ruoyi.web.domain.LockPortInfo;
import com.ruoyi.web.domain.RelPdaUserPort;
import com.ruoyi.web.domain.vo.pda.PdaDataSynchronizationStatusVO;
import com.ruoyi.web.domain.vo.pda.PdaMergeDataVO;
import com.ruoyi.web.enums.PdaDataSynchronizationStatusType;
import com.ruoyi.web.enums.PdaDataSynchronizationType;
import com.ruoyi.web.service.ILockPdaDataSynchronizationInfoService;
import com.ruoyi.web.service.ILockPortInfoService;
import com.ruoyi.web.service.PdaService;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class PdaDataSynchronizationUtil {

    private static final String ADB_PATH = "D:\\out\\platform-tools-latest-windows\\platform-tools\\adb.exe";
    public static final String PDA_DATA_DIR_PATH = "/sdcard/pda/data/";
    private static final String LOCAL_DATA_DIR_PATH = "D:\\out\\";
    private static final String PDA_STATUS_FILENAME = "status.json";
    private static final String PC_DATA_NAME = "pcData.json";
    private static final String PDA_DATA_NAME = "pdaData.json";
    public static final String STATUS_NAME = "status.json";
    public static PdaDataSynchronizationStatusVO statusVO;
    public static String nowStatusMsg;
    public volatile static boolean RUNNING = false;
    public static List<LockPortInfo> resultList = new ArrayList<>();
    public static List<LockPortInfo> updateList = new ArrayList<>();
    public static List<LockPortInfo> oldPcList = new ArrayList<>();
    public static List<LockPortInfo> oldPdaList = new ArrayList<>();
    public static List<LockPortInfo> resolveConflictsList = new ArrayList<>();

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
        nowStatusMsg = PdaDataSynchronizationStatusType.START.getMsg();
        writeStringToFile(JSON.toJSONString(statusVO),
            LOCAL_DATA_DIR_PATH + deviceId + File.separator + STATUS_NAME);
        pushFileToDevice(deviceId, LOCAL_DATA_DIR_PATH + deviceId + File.separator + STATUS_NAME,
            PDA_DATA_DIR_PATH + STATUS_NAME);
        PdaMergeDataVO pdaMergeDataVO;
        //获取所有同步数据
        try {
            PdaService pdaService = SpringUtils.getBean(PdaService.class);
            pdaMergeDataVO = pdaService.getAllData();
        } catch (Exception e) {
            setNowStatusMsgAndAddProcess(synchronizationInfo,
                PdaDataSynchronizationStatusType.PDA_CREATE_DATA);
            PdaDataSynchronizationStopThread.RUNNING = false;
            throw new ServiceException(e.getMessage());
        }

        //等待PDA创建数据文件完成
        setNowStatusMsgAndAddProcess(synchronizationInfo,
            PdaDataSynchronizationStatusType.PDA_CREATE_DATA);
        waitPdaCreateData(deviceId);
        //获取PDA创建文件内容
        setNowStatusMsgAndAddProcess(synchronizationInfo,
            PdaDataSynchronizationStatusType.PC_GET_DATA);
        PdaMergeDataVO fromPdaData = getFromPda(deviceId);
        //创建需要同步给PDA的数据
        setNowStatusMsgAndAddProcess(synchronizationInfo,
            PdaDataSynchronizationStatusType.PC_CREATE_DATA);
        createGivePdaData(pdaMergeDataVO, fromPdaData, deviceId);
        setNowStatusMsgAndAddProcess(synchronizationInfo,
            PdaDataSynchronizationStatusType.PDA_GET_DATA);
        writeDataToPda(deviceId, pdaMergeDataVO);
        getPdaGetDataFlag(deviceId);
        getEndFlag(deviceId);
        updatePortInfo();
        setNowStatusMsgAndAddProcess(synchronizationInfo, PdaDataSynchronizationStatusType.END);
        refreshRunningAndList();
    }

    private static void updatePortInfo() {
        ILockPortInfoService lockPortInfoService = SpringUtils.getBean(ILockPortInfoService.class);
        if (updateList.size() > 0) {
            lockPortInfoService.updateBatchById(updateList);
        }
        if (resolveConflictsList.size() > 0) {
            lockPortInfoService.updateBatchById(resolveConflictsList);
        }
    }

    private static void waitPdaCreateData(String deviceId) {
        while (!statusVO.getPdaCreateDataFlag()) {
            getPdaStatus(deviceId, PdaDataSynchronizationStatusType.PDA_CREATE_DATA);
        }
    }

    private static void getPdaStatus(String deviceId, PdaDataSynchronizationStatusType statusType) {
        try {
            checkDeviceId(deviceId);
            statusVO = JSON.parseObject(
                readFileContentFromDevice(deviceId, PDA_DATA_DIR_PATH + PDA_STATUS_FILENAME),
                PdaDataSynchronizationStatusVO.class);
        } catch (Exception e) {

        } finally {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static PdaMergeDataVO getFromPda(String deviceId) {
        PdaMergeDataVO fromPdaData;
        int error = 0;
        while (true) {
            try {
                fromPdaData = JSON.parseObject(
                    readFileContentFromDevice(deviceId, PDA_DATA_DIR_PATH + PDA_DATA_NAME),
                    PdaMergeDataVO.class);
                if (error > 5) {
                    PdaDataSynchronizationStopThread.RUNNING = false;
                    break;
                }
            } catch (Exception e) {
                error++;
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

    private static void createGivePdaData(PdaMergeDataVO pdaMergeDataVO, PdaMergeDataVO fromPdaData,
        String deviceId) {
        List<LockPortInfo> pcPortList = pdaMergeDataVO.getPortInfoList();
        List<LockPortInfo> pdaPortList = fromPdaData.getPortInfoList();
        ILockPdaDataSynchronizationInfoService synchronizationInfoService = SpringUtils.getBean(
            ILockPdaDataSynchronizationInfoService.class);
        LockPdaDataSynchronizationInfo synchronizationInfo = synchronizationInfoService.getLastByDeviceId(
            deviceId);
        if (null == synchronizationInfo) {
            return;
        }
        long syncTime = synchronizationInfo.getUpdateTime().getTime();
        Map<Integer, LockPortInfo> pcPortMap = pcPortList.stream()
            .collect(Collectors.toMap(LockPortInfo::getId, Function.identity()));
        Map<Integer, LockPortInfo> pdaPortMap = pdaPortList.stream()
            .collect(Collectors.toMap(LockPortInfo::getId, Function.identity()));
        pcPortMap.forEach((key, value) -> {
            if (!pdaPortMap.containsKey(key)) {
                resultList.add(value);
            } else {
                long pcUpdateTime = value.getUpdateTime().getTime();
                LockPortInfo pdaPortInfo = pdaPortMap.get(key);
                long pdaUpdateTime = pdaPortInfo.getUpdateTime().getTime();
                if (pcUpdateTime != pdaUpdateTime) {
                    if (pcUpdateTime > syncTime && pdaUpdateTime > syncTime) {
                        if (!StringUtils.equals(value.getUserCode(), pdaPortInfo.getUserCode())
                            || value.getDeploymentStatus() != pdaPortInfo.getDeploymentStatus()
                            || value.getLockStatus() != pdaPortInfo.getLockStatus()) {
                            oldPcList.add(value);
                            oldPdaList.add(pdaPortInfo);
                        }
                        //判断几个字段是否冲突
                    } else if (pcUpdateTime > syncTime && syncTime <= pdaUpdateTime) {
                        resultList.add(value);
                    } else if (pcUpdateTime <= syncTime && syncTime < pdaUpdateTime) {
                        updateList.add(pdaPortInfo);
                    }
                }
            }
        });
        if (oldPcList.size() > 0 || oldPdaList.size() > 0) {
            while (resolveConflictsList.size() == 0) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (resultList.size() > 0 || resolveConflictsList.size() > 0) {
            pdaMergeDataVO.setPortInfoList(resultList);
            pdaMergeDataVO.getPortInfoList().addAll(resolveConflictsList);
        }
    }


    private static void writeDataToPda(String deviceId, PdaMergeDataVO givePdaData) {
        int i = 0;
        while (!statusVO.getPcCreateDataFlag()) {
            if (i == 0) {
                writeStringToFile(JSON.toJSONString(givePdaData),
                    LOCAL_DATA_DIR_PATH + deviceId + File.separator + PC_DATA_NAME);
                pushFileToDevice(deviceId,
                    LOCAL_DATA_DIR_PATH + deviceId + File.separator + PC_DATA_NAME,
                    PDA_DATA_DIR_PATH + PC_DATA_NAME);
                statusVO.setPcCreateDataFlag(true);
                writeStringToFile(JSON.toJSONString(givePdaData),
                    LOCAL_DATA_DIR_PATH + deviceId + File.separator + PDA_STATUS_FILENAME);
                pushFileToDevice(deviceId,
                    LOCAL_DATA_DIR_PATH + deviceId + File.separator + PDA_STATUS_FILENAME,
                    PDA_DATA_DIR_PATH + PDA_STATUS_FILENAME);
            }
            getPdaStatus(deviceId, PdaDataSynchronizationStatusType.PC_CREATE_DATA);
            i++;
            if (i > 10) {
                i = 0;
            }
        }
    }

    private static void getPdaGetDataFlag(String deviceId) {
        int i=0;
        while (!statusVO.getPdaGetDataFlag()&&i<20) {
            getPdaStatus(deviceId, PdaDataSynchronizationStatusType.PC_GET_DATA);
            i++;
        }
    }

    private static void getEndFlag(String deviceId) {
        while (!statusVO.getEndFlag()) {
            getPdaStatus(deviceId, PdaDataSynchronizationStatusType.END);
        }
    }

    private static void checkDeviceId(String deviceId) {
        if (StringUtils.equals(getConnectedDeviceId(), deviceId)) {
            PdaDataSynchronizationStopThread.RUNNING = false;
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

    public static void emptyList() {
        resultList = new ArrayList<>();
        updateList = new ArrayList<>();
        oldPcList = new ArrayList<>();
        oldPdaList = new ArrayList<>();
        resolveConflictsList = new ArrayList<>();
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
        refreshRunningAndList();
    }

    public static void refreshAll() {
        PdaDataSynchronizationUtil.RUNNING = false;
        PdaDataSynchronizationUtil.statusVO = new PdaDataSynchronizationStatusVO();
        PdaDataSynchronizationUtil.nowStatusMsg = "未同步";
        PdaDataSynchronizationUtil.emptyList();
    }

    public static void refreshRunningAndList() {
        PdaDataSynchronizationUtil.RUNNING = false;
        PdaDataSynchronizationUtil.emptyList();
    }

}
