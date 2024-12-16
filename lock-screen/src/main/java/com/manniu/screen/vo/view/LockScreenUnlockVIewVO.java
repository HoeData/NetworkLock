package com.manniu.screen.vo.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;

@Data
public class LockScreenUnlockVIewVO {

    private Integer id;
    private String deviceId;
    private String lockId;
    private String type;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lockTime;
    private String passwordOrNumber;
    private String userId;
    private String userName;
    private String networkControlName;
    private String electronicLockDescription;


}
