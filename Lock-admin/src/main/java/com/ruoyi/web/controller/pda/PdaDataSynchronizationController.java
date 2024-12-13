package com.ruoyi.web.controller.pda;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Sets;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.web.constants.LockCache;
import com.ruoyi.web.controller.lock.LockPdaInfoController;
import com.ruoyi.web.domain.LockCabinet;
import com.ruoyi.web.domain.LockEquipment;
import com.ruoyi.web.domain.LockEquipmentModel;
import com.ruoyi.web.domain.LockEquipmentType;
import com.ruoyi.web.domain.LockMachineRoom;
import com.ruoyi.web.domain.LockPdaDataSynchronizationInfo;
import com.ruoyi.web.domain.LockPdaInfo;
import com.ruoyi.web.domain.LockPortInfo;
import com.ruoyi.web.domain.vo.pda.PdaDataVO;
import com.ruoyi.web.domain.vo.pda.PdaMergeDataVO;
import com.ruoyi.web.domain.vo.port.LockPortInfoListParamVO;
import com.ruoyi.web.enums.PdaDataSynchronizationStatusType;
import com.ruoyi.web.enums.PdaDataSynchronizationType;
import com.ruoyi.web.service.ILockCabinetService;
import com.ruoyi.web.service.ILockEquipmentModelService;
import com.ruoyi.web.service.ILockEquipmentService;
import com.ruoyi.web.service.ILockEquipmentTypeService;
import com.ruoyi.web.service.ILockMachineRoomService;
import com.ruoyi.web.service.ILockPdaDataSynchronizationInfoService;
import com.ruoyi.web.service.ILockPdaInfoService;
import com.ruoyi.web.service.ILockPortInfoService;
import com.ruoyi.web.service.PdaService;
import com.ruoyi.web.utils.CommonUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/pda/dataSynchronization")
@Slf4j
@Anonymous
@RequiredArgsConstructor
public class PdaDataSynchronizationController {

    private final ILockPdaDataSynchronizationInfoService pdaDataSynchronizationInfoService;
    private final ILockPdaInfoService lockPdaInfoService;
    private final ILockPortInfoService portInfoService;
    private final PdaService pdaService;
    public static final String AES_KEY = "uuNbz89psCnbtJlm";

    @GetMapping("/getAllData/{deviceId}")
    public Object getAllData(@PathVariable String deviceId) {
        return LockPdaInfoController.setSynchronizationMap(lockPdaInfoService.getByKey(deviceId));
    }

    @GetMapping("/saveAll/{deviceId}/{type}")
    public AjaxResult saveAll(@PathVariable String deviceId, @PathVariable Integer type) {
        return AjaxResult.success(pdaDataSynchronizationInfoService.saveAll(deviceId,
            PdaDataSynchronizationType.getEnum(type)));
    }

    @PostMapping("/update")
    public void update(@RequestBody LockPdaDataSynchronizationInfo lockPdaDataSynchronizationInfo) {
        pdaDataSynchronizationInfoService.updateStatus(lockPdaDataSynchronizationInfo,
            lockPdaDataSynchronizationInfo.getStatus());
    }

    @PostMapping("/updatePdaData")
    public void updatePdaData(@RequestBody PdaMergeDataVO pdaMergeDataVO) {
        pdaService.update(pdaMergeDataVO);
    }


    @PostMapping("/updatePdaDataForFile")
    public AjaxResult updatePdaDataForFile(@RequestParam("file") MultipartFile file) {
        LockPdaDataSynchronizationInfo lockPdaDataSynchronizationInfo = null;
        try {
            String content = new BufferedReader(
                new InputStreamReader(file.getInputStream())).lines()
                .collect(Collectors.joining("\n"));
            AES aes = SecureUtil.aes(AES_KEY.getBytes());
            PdaDataVO fromPdaData = JSON.parseObject(aes.decryptStr(content), PdaDataVO.class);
            LockPdaInfo pdaInfo = lockPdaInfoService.getById(fromPdaData.getPdaId());
            PdaMergeDataVO pdaMergeDataVO = pdaService.getAllDataByPda(pdaInfo);
            lockPdaDataSynchronizationInfo = pdaDataSynchronizationInfoService.saveAll(
                pdaInfo.getOnlyKey(), PdaDataSynchronizationType.getEnum(2));
            List<LockPortInfo> portInfoList = portInfoService.getAll(new LockPortInfoListParamVO());
            Map<Integer, LockPortInfo> pcPortMap = new HashMap<>();
            Set<String> lockSet = Sets.newHashSet();
            portInfoList.forEach(portInfo -> {
                pcPortMap.put(portInfo.getId(), portInfo);
                if (StringUtils.isNotBlank(portInfo.getUserCode())) {
                    lockSet.add(portInfo.getUserCode());
                }
            });
            List<LockPortInfo> saveOrUpdateList = Lists.newArrayList();
            fromPdaData.getLockPortInfo().forEach(lockPortInfo -> {
                if (StringUtils.isNotBlank(lockPortInfo.getUserCode())) {
                    lockSet.add(lockPortInfo.getUserCode());
                }
                if (pcPortMap.containsKey(lockPortInfo.getId())) {
                    if (pcPortMap.get(lockPortInfo.getId()).getUpdateTime()
                        .before(lockPortInfo.getUpdateTime())) {
                        saveOrUpdateList.add(lockPortInfo);
                    }
                } else {
                    saveOrUpdateList.add(lockPortInfo);
                }
            });
            if (lockSet.size() > LockCache.lockNumber) {
                pdaDataSynchronizationInfoService.updateStatus(lockPdaDataSynchronizationInfo,
                    PdaDataSynchronizationStatusType.MAXIMUM_NUMBER_EXCEEDED.getValue());
                return AjaxResult.error("同步失败,"
                    + PdaDataSynchronizationStatusType.MAXIMUM_NUMBER_EXCEEDED.getMsg());
            }
            pdaService.synchronization(fromPdaData,pdaMergeDataVO);
            pdaDataSynchronizationInfoService.updateStatus(lockPdaDataSynchronizationInfo,
                PdaDataSynchronizationStatusType.END.getValue());
            return AjaxResult.success("同步成功");
        } catch (Exception e) {
            if (null != lockPdaDataSynchronizationInfo) {
                pdaDataSynchronizationInfoService.updateStatus(lockPdaDataSynchronizationInfo,
                    PdaDataSynchronizationStatusType.ERROR.getValue());
            }
            e.printStackTrace();
            return AjaxResult.error("同步失败");
        }

    }

    @GetMapping("/getStatus")
    public AjaxResult getStatus() {
        return AjaxResult.success();
    }

    @PostMapping("/onlineSynchronization/{deviceId}")
    public PdaMergeDataVO onlineSynchronization(@PathVariable String deviceId,
        @RequestBody PdaDataVO pdaDataVO) {
        LockPdaInfo pdaInfo = lockPdaInfoService.getByKey(deviceId);
        PdaMergeDataVO pdaMergeDataVO = pdaService.getAllDataByPda(pdaInfo);
        return pdaService.synchronization(pdaDataVO, pdaMergeDataVO);
    }
}
