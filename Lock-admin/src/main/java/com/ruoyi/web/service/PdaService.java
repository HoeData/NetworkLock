package com.ruoyi.web.service;

import com.ruoyi.web.domain.vo.pda.PdaMergeDataVO;
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
        return pdaMergeDataVO;
    }

}
