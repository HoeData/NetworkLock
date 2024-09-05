package com.ruoyi.web.domain.vo.pda;

import com.ruoyi.web.domain.LockCabinet;
import com.ruoyi.web.domain.LockCompany;
import com.ruoyi.web.domain.LockDept;
import com.ruoyi.web.domain.LockEquipment;
import com.ruoyi.web.domain.LockEquipmentModel;
import com.ruoyi.web.domain.LockEquipmentType;
import com.ruoyi.web.domain.LockMachineRoom;
import com.ruoyi.web.domain.LockPortInfo;
import com.ruoyi.web.domain.LockSite;
import com.ruoyi.web.domain.RelPdaUserPort;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class PdaMergeDataVO implements Serializable {

    private List<LockCompany> companyList;
    private List<LockSite> siteList;
    private List<LockDept> deptList;
    private List<LockMachineRoom> machineRoomList;
    private List<LockCabinet> cabinetList;
    private List<LockEquipmentType> equipmentTypeList;
    private List<LockEquipmentModel> equipmentModelList;
    private List<LockEquipment> equipmentList;
    private List<LockPortInfo> portInfoList;
    private List<RelPdaUserPort> relPdaUserPortList;

}
