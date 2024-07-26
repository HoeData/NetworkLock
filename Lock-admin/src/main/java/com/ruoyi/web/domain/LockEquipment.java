package com.ruoyi.web.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.web.domain.vo.LockEntity;
import lombok.Data;

@Data
@TableName(value = "lock_equipment")
public class LockEquipment extends LockEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer deptId;
    private Integer cabinetId;
    private Integer equipmentTypeId;
    private Integer equipmentModelId;
    private String name;
    private String description;
    private String delFlag;
    private Integer consoleFlag;
    private Integer portNumber;
}
