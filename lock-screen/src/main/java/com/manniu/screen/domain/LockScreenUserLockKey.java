package com.manniu.screen.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.system.domain.LockEntity;
import lombok.Data;

@TableName(value = "lock_screen_user_lock_key")
@Data
public class LockScreenUserLockKey extends LockEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Long userId;
    @TableField(fill = FieldFill.UPDATE)
    private String carNumber;
    @TableField(fill = FieldFill.UPDATE)
    private String password;
    @TableField(fill = FieldFill.UPDATE)
    private String fingerprint;
    private String delFlag;
}
