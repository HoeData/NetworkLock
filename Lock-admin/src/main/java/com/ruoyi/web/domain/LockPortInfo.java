package com.ruoyi.web.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.web.domain.vo.LockEntity;
import lombok.Data;

@Data
public class LockPortInfo extends LockEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer equipmentId;
    private Integer deploymentStatus;
    private Integer lockStatus;
    private String serialNumber;
    private String remark;
    private String delFlag;
    private String userCode;
}
