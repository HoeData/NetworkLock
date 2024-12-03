package com.ruoyi.web.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.system.domain.LockEntity;
import lombok.Data;

@TableName("lock_warn_info")
@Data
public class LockWarnInfo extends LockEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer portId;
    private Integer status;
    private String delFlag;
}
