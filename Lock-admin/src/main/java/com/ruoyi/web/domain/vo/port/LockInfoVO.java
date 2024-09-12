package com.ruoyi.web.domain.vo.port;

import lombok.Data;

@Data
public class LockInfoVO {
    /**
     * 锁序号
     */
    private byte lockNumber;
    /**
     * 锁序列号
     */
    private byte[] lockSerialNumber;
    /**
     * 锁信息有效期
     */
    private byte lockEffective;
    /**
     * 锁动作时长
     */
    private byte lockTime;
}
