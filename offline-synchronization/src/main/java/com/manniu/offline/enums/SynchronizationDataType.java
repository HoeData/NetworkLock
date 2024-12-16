package com.manniu.offline.enums;


import com.manniu.offline.domain.LockCabinet;
import com.manniu.offline.domain.LockEquipment;
import com.manniu.offline.domain.LockEquipmentModel;
import com.manniu.offline.domain.LockEquipmentType;
import com.manniu.offline.domain.LockMachineRoom;
import com.manniu.offline.domain.LockPortInfo;

public enum SynchronizationDataType {
    MACHINE_ROOM("machineRoomList","lockMachineRoom", LockMachineRoom.class),
    CABINET("cabinetList", "lockCabinet",LockCabinet.class),
    EQUIPMENT_TYPE(
        "equipmentTypeList","lockEquipmentType", LockEquipmentType.class),
    EQUIPMENT_MODEL("equipmentModelList","lockEquipmentModel", LockEquipmentModel.class),
    EQUIPMENT(
        "equipmentList","lockEquipment", LockEquipment.class),
    PORT_INFO("portInfoList","lockPortInfo", LockPortInfo.class),

    ;


    private String fieldName;
    private String  pdaFieldName;
    private Class cla;


    SynchronizationDataType(String fieldName,  String pdaFieldName,Class cla) {
        this.fieldName = fieldName;
        this.cla = cla;
        this.pdaFieldName = pdaFieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Class getCla() {
        return cla;
    }

    public String getPdaFieldName() {
        return pdaFieldName;
    }
}
