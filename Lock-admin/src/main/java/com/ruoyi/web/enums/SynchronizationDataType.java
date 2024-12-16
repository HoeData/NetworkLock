package com.ruoyi.web.enums;


public enum SynchronizationDataType {
    MACHINE_ROOM("machineRoomList", "lockMachineRoom"), CABINET("cabinetList",
        "lockCabinet"), EQUIPMENT_TYPE("equipmentTypeList", "lockEquipmentType"), EQUIPMENT_MODEL(
        "equipmentModelList", "lockEquipmentModel"), EQUIPMENT("equipmentList",
        "lockEquipment"), PORT_INFO("portInfoList", "lockPortInfo"),

    ;


    private String fieldName;
    private String pdaFieldName;

    SynchronizationDataType(String fieldName, String pdaFieldName) {
        this.fieldName = fieldName;
        this.pdaFieldName = pdaFieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getPdaFieldName() {
        return pdaFieldName;
    }
}
