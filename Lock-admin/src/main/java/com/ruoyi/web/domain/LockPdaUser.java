package com.ruoyi.web.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.web.domain.vo.LockEntity;
import lombok.Data;

@Data
@TableName("lock_pda_user")
public class LockPdaUser extends LockEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer pdaId;
    private String userName;
    private String password;
    private String description;
    private String delFlag;
    private Integer adminFlag;
}
