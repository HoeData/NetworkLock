package com.ruoyi.web.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("lock_unlock_log")
public class LockUnlockLog {

    private String id;
    private Integer pdaUserId;
    private Integer portId;
    private String lockSerialNumber;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    private Integer status;
    private String errorMsg;
}
