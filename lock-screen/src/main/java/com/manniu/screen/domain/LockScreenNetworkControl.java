package com.manniu.screen.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.system.domain.LockEntity;
import lombok.Data;

@Data
@TableName(value = "lock_screen_network_control")
public class LockScreenNetworkControl extends LockEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String ip;
    private String description;
    private String companyName;
    private String deptName;
    private String siteName;
    private String machineRoomName;
    private String cabinetName;
    private String deviceId;
    private String delFlag;
}
