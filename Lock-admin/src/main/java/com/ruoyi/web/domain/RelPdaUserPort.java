package com.ruoyi.web.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("rel_pda_user_port")
public class RelPdaUserPort {

    private Integer pdaUserId;
    private Integer portInfoId;

}
