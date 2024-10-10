package com.ruoyi.common.license;

import java.util.List;

public class LicenseParamVO {

    private String batchNo;
    private Integer lockNumber;
    private List<LockLicenseInfoVO> lockInfoList;
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

    public List<LockLicenseInfoVO> getLockInfoList() {
        return lockInfoList;
    }

    public void setLockInfoList(List<LockLicenseInfoVO> lockInfoList) {
        this.lockInfoList = lockInfoList;
    }
}
