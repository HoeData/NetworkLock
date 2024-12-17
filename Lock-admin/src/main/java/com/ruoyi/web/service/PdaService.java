package com.ruoyi.web.service;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.ruoyi.web.aspectj.CompanyScopeAspect;
import com.ruoyi.web.constants.CommonFieldConst;
import com.ruoyi.web.domain.LockCabinet;
import com.ruoyi.web.domain.LockCompany;
import com.ruoyi.web.domain.LockEquipment;
import com.ruoyi.web.domain.LockEquipmentModel;
import com.ruoyi.web.domain.LockEquipmentType;
import com.ruoyi.web.domain.LockMachineRoom;
import com.ruoyi.web.domain.LockPdaInfo;
import com.ruoyi.web.domain.LockPdaUser;
import com.ruoyi.web.domain.LockPortInfo;
import com.ruoyi.web.domain.RelPdaUserPort;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.domain.vo.equipment.LockEquipmentParamVO;
import com.ruoyi.web.domain.vo.pda.LockPdaUserPageParamVO;
import com.ruoyi.web.domain.vo.pda.PdaDataVO;
import com.ruoyi.web.domain.vo.pda.PdaMergeDataVO;
import com.ruoyi.web.domain.vo.pda.RelPdaUserPortParamVO;
import com.ruoyi.web.domain.vo.pda.RelPdaUserPortViewVO;
import com.ruoyi.web.domain.vo.port.LockPortInfoListParamVO;
import com.ruoyi.web.enums.SynchronizationDataType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PdaService {
    private final ILockCompanyService companyService;
    private final ILockDeptService deptService;
    private final ILockSiteService siteService;
    private final ILockMachineRoomService machineRoomService;
    private final ILockCabinetService cabinetService;
    private final ILockEquipmentModelService equipmentModelService;
    private final ILockEquipmentTypeService equipmentTypeService;
    private final ILockEquipmentService equipmentService;
    private final ILockPortInfoService portInfoService;
    private final ILockPdaUserService pdaUserService;
    private final IRelPdaUserPortService relPdaUserPortService;
    private final ILockUnlockLogService unlockLogService;

    public static final String GET = "get";
    private static final String SET = "set";

    public PdaMergeDataVO getAllDataByPda(LockPdaInfo lockPdaInfo) {
        LockCommonParamVO lockCommonParamVO = new LockCommonParamVO();
        LockEquipmentParamVO lockEquipmentParamVO = new LockEquipmentParamVO();
        LockPortInfoListParamVO lockPortInfoListParamVO = new LockPortInfoListParamVO();
        LockCompany lockCompany = companyService.getByIdCache(lockPdaInfo.getCompanyId());
        String sqlString =
            "company.id in (select id from lock_company where path like '%" + lockCompany.getPath()
                + "')";
        lockCommonParamVO.getParamMap().put(CompanyScopeAspect.COMPANY_SCOPE, sqlString);
        lockEquipmentParamVO.getParamMap().put(CompanyScopeAspect.COMPANY_SCOPE, sqlString);
        lockPortInfoListParamVO.getParamMap().put(CompanyScopeAspect.COMPANY_SCOPE, sqlString);
        PdaMergeDataVO pdaMergeDataVO = new PdaMergeDataVO();
        pdaMergeDataVO.setCompanyList(companyService.selectCompanyList(lockCommonParamVO));
        pdaMergeDataVO.setDeptList(deptService.getAll(lockCommonParamVO));
        pdaMergeDataVO.setSiteList(siteService.getAll(lockCommonParamVO));
        pdaMergeDataVO.setMachineRoomList(machineRoomService.getAll(lockCommonParamVO));
        pdaMergeDataVO.setCabinetList(cabinetService.getAll(lockCommonParamVO));
        pdaMergeDataVO.setEquipmentModelList(
            equipmentModelService.selectEquipmentModelList(lockCommonParamVO));
        pdaMergeDataVO.setEquipmentTypeList(
            equipmentTypeService.selectEquipmentTypeList(lockCommonParamVO));
        pdaMergeDataVO.setEquipmentList(equipmentService.getAll(lockEquipmentParamVO));
        pdaMergeDataVO.setPortInfoList(portInfoService.getAll(lockPortInfoListParamVO));
        LockPdaUserPageParamVO vo = new LockPdaUserPageParamVO();
        vo.setPdaId(lockPdaInfo.getId());
        List<LockPdaUser> pdaUserList = pdaUserService.getPdaUserList(vo);
        pdaMergeDataVO.setPdaUserList(pdaUserList);
        List<RelPdaUserPort> relPdaUserPortList = new ArrayList<>();
        pdaUserList.forEach(pdaUser -> {
            RelPdaUserPortParamVO relPdaUserPortParamVO = new RelPdaUserPortParamVO();
            relPdaUserPortParamVO.setPdaUserId(pdaUser.getId());
            List<RelPdaUserPortViewVO> list = relPdaUserPortService.getAuthorizationList(
                relPdaUserPortParamVO);
            list.forEach(item -> {
                RelPdaUserPort relPdaUserPort = new RelPdaUserPort();
                relPdaUserPort.setPdaUserId(pdaUser.getId());
                relPdaUserPort.setPortInfoId(item.getPortInfoId());
                relPdaUserPort.setSerialNumber(item.getSerialNumber());
                relPdaUserPort.setLockSerialNumber(item.getLockSerialNumber());
                relPdaUserPort.setValidityPeriod(item.getValidityPeriod());
                relPdaUserPortList.add(relPdaUserPort);
            });
        });
        pdaMergeDataVO.setRelPdaUserPortList(relPdaUserPortList);
        return pdaMergeDataVO;
    }

    public void update(PdaMergeDataVO pdaMergeDataVO) {
        if (pdaMergeDataVO.getMachineRoomList().size() > 0) {
            machineRoomService.updateBatchById(pdaMergeDataVO.getMachineRoomList());
        }
        if (pdaMergeDataVO.getCabinetList().size() > 0) {
            cabinetService.saveOrUpdateBatch(pdaMergeDataVO.getCabinetList());
        }
        if (pdaMergeDataVO.getEquipmentTypeList().size() > 0) {
            equipmentTypeService.updateBatchById(pdaMergeDataVO.getEquipmentTypeList());
        }
        if (pdaMergeDataVO.getEquipmentList().size() > 0) {
            equipmentModelService.saveOrUpdateBatch(pdaMergeDataVO.getEquipmentModelList());
        }
        if (pdaMergeDataVO.getEquipmentList().size() > 0) {
            equipmentService.saveOrUpdateBatch(pdaMergeDataVO.getEquipmentList());
        }
        if (pdaMergeDataVO.getPortInfoList().size() > 0) {
            portInfoService.updateBatchById(pdaMergeDataVO.getPortInfoList());
        }
        if (pdaMergeDataVO.getUnlockLogList().size() > 0) {
            unlockLogService.saveOrUpdateBatch(pdaMergeDataVO.getUnlockLogList());
        }
    }

    @Transactional
    public PdaMergeDataVO synchronization(PdaDataVO pdaDataVO, PdaMergeDataVO pdaMergeDataVO) {
        Map<String, Integer> idMap = Maps.newHashMap();
        encapsulatedData(pdaMergeDataVO, pdaDataVO, SynchronizationDataType.MACHINE_ROOM,
            LockMachineRoom.class, idMap, machineRoomService);
        encapsulatedData(pdaMergeDataVO, pdaDataVO, SynchronizationDataType.CABINET,
            LockCabinet.class, idMap, cabinetService);
        encapsulatedData(pdaMergeDataVO, pdaDataVO, SynchronizationDataType.EQUIPMENT_TYPE,
            LockEquipmentType.class, idMap, equipmentTypeService);
        encapsulatedData(pdaMergeDataVO, pdaDataVO, SynchronizationDataType.EQUIPMENT_MODEL,
            LockEquipmentModel.class, idMap, equipmentModelService);
        encapsulatedData(pdaMergeDataVO, pdaDataVO, SynchronizationDataType.EQUIPMENT,
            LockEquipment.class, idMap, equipmentService);
        encapsulatedData(pdaMergeDataVO, pdaDataVO, SynchronizationDataType.PORT_INFO,
            LockPortInfo.class, idMap, portInfoService);
        return pdaMergeDataVO;
    }

    private void encapsulatedData(PdaMergeDataVO pdaMergeDataVO, PdaDataVO fromPdaData,
        SynchronizationDataType synchronizationDataType, Class cla, Map<String, Integer> idMap,
        Object service) {
        MethodAccess dataAccess = MethodAccess.get(cla);
        MethodAccess pdaMergeAccess = MethodAccess.get(pdaMergeDataVO.getClass());
        MethodAccess fromPdaAccess = MethodAccess.get(fromPdaData.getClass());
        Object pcValue = pdaMergeAccess.invoke(pdaMergeDataVO, pdaMergeAccess.getIndex(
            GET + StringUtils.capitalize(synchronizationDataType.getFieldName())));
        Object pdaValue = fromPdaAccess.invoke(fromPdaData, fromPdaAccess.getIndex(
            GET + StringUtils.capitalize(synchronizationDataType.getPdaFieldName())));
        List<Object> pcList = Lists.newArrayList();
        List<Object> pdaList = Lists.newArrayList();
        Set<Integer> idSet = Sets.newHashSet();
        if (null != pcValue) {
            pcList = (List<Object>) pcValue;
            pdaList = (List<Object>) pdaValue;
        }
        Map<Integer, Object> pcMap = Maps.newHashMap();
        for (Object pcObJ : pcList) {
            pcMap.put((Integer) dataAccess.invoke(pcObJ,
                dataAccess.getIndex(GET + StringUtils.capitalize(CommonFieldConst.ID))), pcObJ);
        }
        List<Object> updateList = Lists.newArrayList();
        List<Object> endList = Lists.newArrayList();
        boolean needUpdate = false;
        for (Object objPda : pdaList) {
            Integer pdaDataId = (Integer) dataAccess.invoke(objPda,
                dataAccess.getIndex(GET + StringUtils.capitalize(CommonFieldConst.ID)));
            if (null == dataAccess.invoke(objPda,
                dataAccess.getIndex(GET + StringUtils.capitalize(CommonFieldConst.CREATE_BY)))) {
                dataAccess.invoke(objPda,
                    dataAccess.getIndex(SET + StringUtils.capitalize(CommonFieldConst.ID),
                        Integer.class), 0);
                dataAccess.invoke(objPda,
                    dataAccess.getIndex(SET + StringUtils.capitalize(CommonFieldConst.CREATE_BY),
                        String.class), "1");
                endList.add(
                    encapsulatedSimpleData(synchronizationDataType, objPda, idMap, pdaDataId,
                        service));
            } else {
                idSet.add(pdaDataId);
                Date pcUpdateTime = (Date) dataAccess.invoke(pcMap.get(pdaDataId),
                    dataAccess.getIndex(
                        GET + StringUtils.capitalize(CommonFieldConst.UPDATE_TIME)));
                Date pdaUpdateTime = (Date) dataAccess.invoke(objPda, dataAccess.getIndex(
                    GET + StringUtils.capitalize(CommonFieldConst.UPDATE_TIME)));
                if (pcMap.containsKey(pdaDataId)
                    && pcUpdateTime.getTime() < pdaUpdateTime.getTime()) {
                    updateList.add(objPda);
                    needUpdate = true;
                } else {
                    endList.add(pcMap.get(pdaDataId));
                }
            }
        }
        List<Object> allList = Lists.newArrayList();
        pcMap.forEach((k, v) -> {
            if (!idSet.contains(k)) {
                allList.add(v);
            }
        });
        if (needUpdate) {
            MethodAccess serviceMethodAccess = MethodAccess.get(service.getClass());
            serviceMethodAccess.invoke(service,
                serviceMethodAccess.getIndex("saveOrUpdateForSynchronization", List.class),
                updateList);
        }
        allList.addAll(updateList);
        allList.addAll(endList);
        pdaMergeAccess.invoke(pdaMergeDataVO, pdaMergeAccess.getIndex(
                SET + StringUtils.capitalize(synchronizationDataType.getFieldName()), List.class),
            allList);
    }

    private Object encapsulatedSimpleData(SynchronizationDataType synchronizationDataType,
        Object pdaObj, Map<String, Integer> idMap, Integer pdaDataId, Object service) {
        MethodAccess dataAccess = MethodAccess.get(pdaObj.getClass());
        switch (synchronizationDataType) {
            case CABINET:
                Integer oldRoomId = (Integer) dataAccess.invoke(pdaObj, dataAccess.getIndex(
                    GET + StringUtils.capitalize(CommonFieldConst.MACHINE_ROOM_ID)));
                Integer newId = idMap.get(
                    synchronizationDataType.MACHINE_ROOM.getFieldName() + oldRoomId);
                dataAccess.invoke(pdaObj, dataAccess.getIndex(
                        SET + StringUtils.capitalize(CommonFieldConst.MACHINE_ROOM_ID), Integer.class),
                    newId);
                break;
            case EQUIPMENT:
                dataAccess.invoke(pdaObj,
                    dataAccess.getIndex(SET + StringUtils.capitalize(CommonFieldConst.CABINET_ID),
                        Integer.class), idMap.get(
                        synchronizationDataType.CABINET.getFieldName() + dataAccess.invoke(pdaObj,
                            dataAccess.getIndex(
                                GET + StringUtils.capitalize(CommonFieldConst.CABINET_ID)))));
                dataAccess.invoke(pdaObj, dataAccess.getIndex(
                    SET + StringUtils.capitalize(CommonFieldConst.EQUIPMENT_TYPE_ID),
                    Integer.class), idMap.get(
                    synchronizationDataType.EQUIPMENT_TYPE.getFieldName() + dataAccess.invoke(
                        pdaObj, dataAccess.getIndex(
                            GET + StringUtils.capitalize(CommonFieldConst.EQUIPMENT_TYPE_ID)))));
                dataAccess.invoke(pdaObj, dataAccess.getIndex(
                    SET + StringUtils.capitalize(CommonFieldConst.EQUIPMENT_MODEL_ID),
                    Integer.class), idMap.get(
                    synchronizationDataType.EQUIPMENT_MODEL.getFieldName() + dataAccess.invoke(
                        pdaObj, dataAccess.getIndex(
                            GET + StringUtils.capitalize(CommonFieldConst.EQUIPMENT_MODEL_ID)))));
                break;
            case PORT_INFO:
                Integer oldEquipmentId = (Integer) dataAccess.invoke(pdaObj, dataAccess.getIndex(
                    GET + StringUtils.capitalize(CommonFieldConst.EQUIPMENT_ID)));
                dataAccess.invoke(pdaObj,
                    dataAccess.getIndex(SET + StringUtils.capitalize(CommonFieldConst.EQUIPMENT_ID),
                        Integer.class),
                    idMap.get(synchronizationDataType.EQUIPMENT.getFieldName() + oldEquipmentId));
        }
        return setIdMapAndSave(synchronizationDataType, pdaObj, idMap, pdaDataId, dataAccess,
            service);
    }

    private static Object setIdMapAndSave(SynchronizationDataType synchronizationDataType,
        Object pdaObj, Map<String, Integer> idMap, Integer pdaDataId, MethodAccess dataAccess,
        Object service) {
        MethodAccess serviceMethodAccess = MethodAccess.get(service.getClass());
        List<Object> list=Lists.newArrayList(pdaObj);
        serviceMethodAccess.invoke(service,
            serviceMethodAccess.getIndex("saveOrUpdateForSynchronization", List.class), list);
        idMap.put(synchronizationDataType.getFieldName() + pdaDataId,
            (Integer) dataAccess.invoke(pdaObj,
                dataAccess.getIndex(GET + StringUtils.capitalize("id"))));
        return pdaObj;
    }
}
