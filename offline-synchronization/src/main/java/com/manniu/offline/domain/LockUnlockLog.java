package com.manniu.offline.domain;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class LockUnlockLog {

    private String id;
    private Integer pdaUserId;
    private Integer portId;
    private String lockSerialNumber;
    private LocalDateTime createTime;
    private Integer status;
    private String errorMsg;
}
