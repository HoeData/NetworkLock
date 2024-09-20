package com.ruoyi.web.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.web.domain.vo.LockEntity;
import lombok.Data;

@Data
@TableName("lock_terminal_security_equipment")
public class LockTerminalSecurityEquipment extends LockEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer equipmentId;
    private String remark;
    private String delFlag;
}
