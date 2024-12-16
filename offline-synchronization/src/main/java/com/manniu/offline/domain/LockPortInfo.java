package com.manniu.offline.domain;

import lombok.Data;

@Data
public class LockPortInfo extends LockEntity {

    private Integer id;
    private Integer equipmentId;
    private Integer deploymentStatus;
    private String serialNumber;
    private String remark;
    private String delFlag;
    private String userCode;
    private Integer pdaUserId;
    private Integer otherStatus;
}
