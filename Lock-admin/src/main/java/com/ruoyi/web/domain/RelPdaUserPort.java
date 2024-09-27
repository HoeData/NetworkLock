package com.ruoyi.web.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;

@Data
@TableName("rel_pda_user_port")
public class RelPdaUserPort {

    private Integer pdaUserId;
    private Integer portInfoId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date validityPeriod;
    private Date createTime;

}
