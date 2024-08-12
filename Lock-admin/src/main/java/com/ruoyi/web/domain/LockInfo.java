package com.ruoyi.web.domain;

import lombok.Data;

@Data
public class LockInfo {
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
