package com.manniu.screen.vo.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class LockScreenUserLockPermissionsViewVO {
    private Integer id;
    private Integer userId;
    private String userName;
    private Integer networkControlId;
    private String networkControlName;
    private String ip;
    private String deviceId;
    private Integer electronicLockId;
    private Integer electronicLockDescription;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    private Integer lockId;
    private Integer authorizationFlag;
    private String authorizationFlagStr;
}
