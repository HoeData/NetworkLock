package com.manniu.screen.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.system.domain.LockEntity;
import javax.validation.constraints.NotNull;
import lombok.Data;

@TableName(value = "lock_screen_user_lock_key")
@Data
public class LockScreenUserLockKey extends LockEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String contactInformation;
    private String remark;
    @TableField(fill = FieldFill.UPDATE)
    private String cardNumber;
    @TableField(fill = FieldFill.UPDATE)
    private String password;
    @TableField(fill = FieldFill.UPDATE)
    private String fingerprint;
    private String delFlag;
    @TableField(exist = false)
    private Integer type;
    private Integer realAccountId;
}
