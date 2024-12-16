package com.ruoyi.web.domain.vo.pda;

import com.ruoyi.web.domain.LockCabinet;
import com.ruoyi.web.domain.LockCompany;
import com.ruoyi.web.domain.LockDept;
import com.ruoyi.web.domain.LockEquipment;
import com.ruoyi.web.domain.LockEquipmentModel;
import com.ruoyi.web.domain.LockEquipmentType;
import com.ruoyi.web.domain.LockMachineRoom;
import com.ruoyi.web.domain.LockPdaUser;
import com.ruoyi.web.domain.LockPortInfo;
import com.ruoyi.web.domain.LockSite;
import com.ruoyi.web.domain.LockUnlockLog;
import com.ruoyi.web.domain.RelPdaUserPort;
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

    public PdaMergeDataVO getByPdaData(PdaDataVO pdaDataVO) {
        PdaMergeDataVO pdaMergeDataVO = new PdaMergeDataVO();
        pdaMergeDataVO.setMachineRoomList(pdaDataVO.getLockMachineRoom());
        pdaMergeDataVO.setCabinetList(pdaDataVO.getLockCabinet());
        pdaMergeDataVO.setEquipmentTypeList(pdaDataVO.getLockEquipmentType());
        pdaMergeDataVO.setEquipmentModelList(pdaDataVO.getLockEquipmentModel());
        pdaMergeDataVO.setEquipmentList(pdaDataVO.getLockEquipment());
        pdaMergeDataVO.setPortInfoList(pdaDataVO.getLockPortInfo());
        pdaMergeDataVO.setUnlockLogList(pdaDataVO.getLockUnlockLog());
        return pdaMergeDataVO;
    }

}
