package com.ruoyi.web.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.web.domain.LockPdaUser;
import com.ruoyi.web.domain.LockPortInfo;
import com.ruoyi.web.domain.LockUnlockLog;
import com.ruoyi.web.domain.RelPdaUserPort;
import com.ruoyi.web.domain.vo.pda.LockPdaUserPageParamVO;
import com.ruoyi.web.domain.vo.pda.PdaDataVO;
import com.ruoyi.web.domain.vo.pda.RelPdaUserPortParamVO;
import com.ruoyi.web.domain.vo.pda.RelPdaUserPortViewVO;
import com.ruoyi.web.domain.vo.pda.PdaMergeDataVO;
import com.ruoyi.web.utils.PdaDataSynchronizationUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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

    public PdaMergeDataVO getAllData() {
        PdaMergeDataVO pdaMergeDataVO = new PdaMergeDataVO();
        pdaMergeDataVO.setCompanyList(companyService.getAll());
        pdaMergeDataVO.setDeptList(deptService.getAll());
        pdaMergeDataVO.setSiteList(siteService.getAll());
        pdaMergeDataVO.setMachineRoomList(machineRoomService.getAll());
        pdaMergeDataVO.setCabinetList(cabinetService.getAll());
        pdaMergeDataVO.setEquipmentModelList(equipmentModelService.getAll());
        pdaMergeDataVO.setEquipmentTypeList(equipmentTypeService.getAll());
        pdaMergeDataVO.setEquipmentList(equipmentService.getAll());
        pdaMergeDataVO.setPortInfoList(portInfoService.getAll());
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
