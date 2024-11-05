package com.manniu.offline.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class PdaMergeDataVO implements Serializable {

    private List<LockCompany> companyList = new ArrayList<>();
    private List<LockSite> siteList = new ArrayList<>();
    private List<LockDept> deptList = new ArrayList<>();
    private List<LockMachineRoom> machineRoomList = new ArrayList<>();
    private List<LockCabinet> cabinetList = new ArrayList<>();
    private List<LockEquipmentType> equipmentTypeList = new ArrayList<>();
    private List<LockEquipmentModel> equipmentModelList = new ArrayList<>();
    private List<LockEquipment> equipmentList = new ArrayList<>();
    private List<LockPortInfo> portInfoList = new ArrayList<>();
    private List<LockPdaUser> pdaUserList = new ArrayList<>();
    private List<RelPdaUserPort> relPdaUserPortList = new ArrayList<>();
    private List<LockUnlockLog> unlockLogList = new ArrayList<>();

}
