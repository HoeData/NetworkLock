package com.manniu.offline.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("lock_info")
public class LockInfo {
    @TableId
    private String serialNumber;
    private String batchNo;
    private Integer type;
}
