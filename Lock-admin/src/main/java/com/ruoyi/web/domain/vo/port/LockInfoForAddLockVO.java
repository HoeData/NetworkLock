package com.ruoyi.web.domain.vo.port;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.Data;

@Data
public class LockInfoForAddLockVO {

    /**
     * 锁序号
     */
    private Integer lockNumber;
    /**
     * 锁序列号
     */
    private String lockSerialNumber;
    /**
     * 锁信息有效期
     */
    @Max(value = 255, message = "有效期不能大于255")
    @Min(value = 0, message = "有效期不能小于0")
    private Integer lockEffective;
    /**
     * 锁动作时长
     */
    @Max(value = 8, message = "动作时长不能大于8")
    @Min(value = 1, message = "动作时长不能小于1")
    private Integer lockTime;
}
