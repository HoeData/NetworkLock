package com.manniu.screen.vo.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
}
