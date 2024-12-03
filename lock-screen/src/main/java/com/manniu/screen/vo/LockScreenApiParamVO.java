package com.manniu.screen.vo;

import lombok.Data;

@Data
public class LockScreenApiParamVO {

    /**
     * ip
     */
    private String ip;
    /**
     * 设备id
     */
    private String deviceId;
    /**
     * 锁具id
     */
    private Integer lockId;

    /**
     * 用户id
     */
    private String accountId;
    /**
     * 密码
     */
    private String password;

    /**
     * 指纹
     */
    private String fingerprint;
    /**
     * 卡号
     */
    private String cardNumber;

    /**
     * 类型 1写入 2删除
     */
    private Integer type;

}
