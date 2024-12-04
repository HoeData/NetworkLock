package com.manniu.screen.vo.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class LockScreenUserLockPermissionsViewVO {

    private Integer id;
    private Long userId;
    private String userName;
    private String carNumber;
    private Integer electronicLockId;
    private String electronicLockSerialNumber;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
