package com.manniu.screen.vo.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.manniu.screen.vo.PageVO;
import java.util.Date;
import lombok.Data;

@Data
public class LockScreenUnlockPageParamVO extends PageVO {

    private String ip;
    private String deviceId;
    private String lockId;
    private String userName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startLockTIme;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endLockTIme;
}
