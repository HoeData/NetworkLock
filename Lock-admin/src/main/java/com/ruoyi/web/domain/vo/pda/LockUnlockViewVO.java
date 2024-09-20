package com.ruoyi.web.domain.vo.pda;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class LockUnlockViewVO {
    private String id;
    private Integer pdaUserId;
    private String pdaUserName;
    private String portIndex;
    private Integer portId;
    private String equipmentName;
    private String lockSerialNumber;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


}
