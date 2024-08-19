package com.ruoyi.web.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.web.domain.vo.LockEntity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.Data;

@Data
public class LockPortInfo extends LockEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer equipmentId;
    private Integer deploymentStatus;
    private Integer lockStatus;
    private Integer serialNumber;
    private String remark;
    private String delFlag;
    private String userCode;
    private String keyId;
    @Max(value = 255, message = "有效期不能大于255")
    @Min(value = 0, message = "有效期不能小于0")
    private Integer validityPeriod;
    @Max(value = 8, message = "动作时长不能大于8")
    @Min(value = 1, message = "动作时长不能小于1")
    private Integer actionDuration;
}
