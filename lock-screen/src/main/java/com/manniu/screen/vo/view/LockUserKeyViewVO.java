package com.manniu.screen.vo.view;

import lombok.Data;

@Data
public class LockUserKeyViewVO {

    private Integer userId;
    private String name;
    private String contactInformation;
    private String remark;
    private String cardNumber;
    private String password;
    private String fingerprint;
    private Integer cardNumberStatus;
    private Integer passwordStatus;
    private Integer fingerprintStatus;
}
