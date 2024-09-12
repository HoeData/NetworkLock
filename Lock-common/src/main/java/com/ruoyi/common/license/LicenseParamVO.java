package com.ruoyi.common.license;

import java.util.List;

public class LicenseParamVO {

    private String batchNo;
    private Integer lockNumber;

    private List<String> lockSerialNumberList;
    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Integer getLockNumber() {
        return lockNumber;
    }

    public void setLockNumber(Integer lockNumber) {
        this.lockNumber = lockNumber;
    }

    public List<String> getLockSerialNumberList() {
        return lockSerialNumberList;
    }

    public void setLockSerialNumberList(List<String> lockSerialNumberList) {
        this.lockSerialNumberList = lockSerialNumberList;
    }
}
