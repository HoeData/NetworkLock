package com.ruoyi.web.enums;

public enum PdaDataSynchronizationType {
    DATA_SYNCHRONIZATION(1, "数据同步"),
    AUTHORIZATION_SYNCHRONIZATION(2, "授权同步"),
    USER_INFORMATION_SYNCHRONIZATION(3, "用户信息同步"),
    ;


    private Integer value;
    private String msg;


    PdaDataSynchronizationType(Integer value, String msg) {
        this.value = value;
        this.msg = msg;

    }

    public Integer getValue() {
        return value;
    }

    public String getMsg() {
        return msg;
    }

    public static PdaDataSynchronizationType getEnum(int value) {
        for (PdaDataSynchronizationType type : PdaDataSynchronizationType.values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }
}
