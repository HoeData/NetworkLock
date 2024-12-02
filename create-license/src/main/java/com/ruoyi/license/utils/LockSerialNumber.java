package com.ruoyi.license.utils;

import com.alibaba.excel.annotation.ExcelProperty;

public class LockSerialNumber {
    @ExcelProperty(index = 0)
    private String serialNumber;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}
