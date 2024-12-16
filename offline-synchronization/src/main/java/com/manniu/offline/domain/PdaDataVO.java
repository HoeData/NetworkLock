package com.manniu.offline.domain;

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
