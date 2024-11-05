package com.manniu.offline.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.manniu.domain.vo.LockEntity;
import lombok.Data;

@TableName(value = "lock_dept")
@Data
public class LockDept extends LockEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer companyId;
    private String name;
    private String description;
    private String delFlag;
}
