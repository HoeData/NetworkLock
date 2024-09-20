package com.ruoyi.web.domain.vo.pda;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;

@Data
public class UnlockViewVO {

    private Integer id;
    private Integer pdaUserId;
    private String pdaDescription;
    private String pdaUserName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private Integer successFlag;
    private String  errorMsg;
}
