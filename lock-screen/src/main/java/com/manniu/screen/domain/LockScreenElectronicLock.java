package com.manniu.screen.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.system.domain.LockEntity;
import javax.validation.constraints.NotNull;
import lombok.Data;

@TableName("lock_screen_electronic_lock")
@Data
public class LockScreenElectronicLock extends LockEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;
    @NotNull(message = "锁ID不能为空")
    private Integer lockId;
    private String description;
    @NotNull(message = "所属控制器为空")
    private Integer networkControlId;
    private String delFlag;
    private String userType;
}
