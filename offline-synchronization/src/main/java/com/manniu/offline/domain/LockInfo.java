package com.manniu.offline.domain;

import lombok.Data;

@Data
public class LockInfo {
    private String serialNumber;
    private String batchNo;
    private Integer type;
}
