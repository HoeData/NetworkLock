package com.manniu.screen.enums;

public enum EquipmentType {
    NETWORK_CONTROLLER(1),
    ELECTRONIC_LOCK(2),
    ;


    private Integer value;

    EquipmentType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
