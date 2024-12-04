package com.manniu.screen.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.system.domain.LockEntity;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName(value = "lock_screen_user_lock_permissions")
public class LockScreenUserLockPermissions extends LockEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Long userId;
    private String carNumber;
    private Integer electronicLockId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}
