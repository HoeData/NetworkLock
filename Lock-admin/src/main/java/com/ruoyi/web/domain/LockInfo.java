package com.ruoyi.web.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

@Data
@TableName("lock_info")
public class LockInfo extends BaseEntity {

    private String id;
    private String serialNumber;
    private String batchNo;
}
