package com.ruoyi.web.service;

import com.ruoyi.web.aspectj.CompanyScopeAspect;
import com.ruoyi.web.domain.LockCompany;
import com.ruoyi.web.domain.LockPdaInfo;
import com.ruoyi.web.domain.LockPdaUser;
import com.ruoyi.web.domain.RelPdaUserPort;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.domain.vo.equipment.LockEquipmentParamVO;
import com.ruoyi.web.domain.vo.pda.LockPdaUserPageParamVO;
import com.ruoyi.web.domain.vo.pda.PdaDataVO;
import com.ruoyi.web.domain.vo.pda.PdaMergeDataVO;
import com.ruoyi.web.domain.vo.pda.RelPdaUserPortParamVO;
import com.ruoyi.web.domain.vo.pda.RelPdaUserPortViewVO;
import com.ruoyi.web.domain.vo.port.LockPortInfoListParamVO;
import com.ruoyi.web.utils.PdaDataSynchronizationUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    private final ILockPdaInfoService pdaService;
    private final ILockPdaUserService pdaUserService;
    private final IRelPdaUserPortService relPdaUserPortService;
    private final ILockUnlockLogService unlockLogService;

    public PdaMergeDataVO getAllDataByPda(LockPdaInfo lockPdaInfo) {
        LockCommonParamVO lockCommonParamVO = new LockCommonParamVO();
        LockEquipmentParamVO lockEquipmentParamVO = new LockEquipmentParamVO();
        LockPortInfoListParamVO lockPortInfoListParamVO = new LockPortInfoListParamVO();
        LockCompany lockCompany = companyService.getByIdCache(lockPdaInfo.getCompanyId());
        String sqlString = "company.id in (select id from lock_company where path like '%"
            + lockCompany.getPath() + "')";
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
        vo.setPdaId(pdaService.getByKey(PdaDataSynchronizationUtil.getConnectedDeviceId()).getId());
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

    public void update(PdaDataVO fromPdaData) {
        if(fromPdaData.getLockPortInfo().size()>0){
            portInfoService.updateBatchById(fromPdaData.getLockPortInfo());
        }
        if(fromPdaData.getLockUnlockLog().size()>0){
            unlockLogService.saveOrUpdateBatch(fromPdaData.getLockUnlockLog());
        }
    }
}
