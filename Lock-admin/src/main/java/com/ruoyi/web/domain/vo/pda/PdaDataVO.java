package com.ruoyi.web.domain.vo.pda;

import com.ruoyi.web.domain.LockCabinet;
import com.ruoyi.web.domain.LockEquipment;
import com.ruoyi.web.domain.LockEquipmentModel;
import com.ruoyi.web.domain.LockEquipmentType;
import com.ruoyi.web.domain.LockMachineRoom;
import com.ruoyi.web.domain.LockPortInfo;
import com.ruoyi.web.domain.LockUnlockLog;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class PdaDataVO implements Serializable {

    private List<LockPortInfo> lockPortInfo = new ArrayList<>();
    private List<LockUnlockLog> lockUnlockLog = new ArrayList<>();
    private List<LockMachineRoom> lockMachineRoom = new ArrayList<>();
    private List<LockCabinet> lockCabinet = new ArrayList<>();
    private List<LockEquipmentType> lockEquipmentType = new ArrayList<>();
    private List<LockEquipmentModel> lockEquipmentModel = new ArrayList<>();
    private List<LockEquipment> lockEquipment = new ArrayList<>();
    private String pdaId;


}
