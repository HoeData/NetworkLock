package com.ruoyi.web.enums;

public enum PdaDataSynchronizationStatusType {
    START(0, "开始同步"),
    NO_MATCH(-1, "PDA不匹配"),
    PDA_CREATE_DATA(1, "pda创建同步文件失败"),
    PC_GET_DATA(2, "PC获取PDA数据文件失败"),
    PC_CREATE_DATA(3, "PC创建同步文件失败"),
    PDA_GET_DATA(4, "pda获取同步文件失败"),
    END(5, "同步成功"),
    MAXIMUM_NUMBER_EXCEEDED(6, "超过最大允许管理锁数量"),
    ;


    private Integer value;
    private String msg;

    PdaDataSynchronizationStatusType(Integer value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    public Integer getValue() {
        return value;
    }

    public String getMsg() {
        return msg;
    }
}
