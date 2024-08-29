package com.ruoyi.web.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;

@TableName("lock_monitor_flow")
@Data
public class LockMonitorFlow {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer portId;
    private String inFlow;
    private String outFlow;
    private String delFlag;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
