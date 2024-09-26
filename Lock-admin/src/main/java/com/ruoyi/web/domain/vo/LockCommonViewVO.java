package com.ruoyi.web.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;

@Data
public class LockCommonViewVO {
    private String name;
    private Integer id;
    private Integer companyId;
    private String companyName;
    private Integer siteId;
    private String siteName;
    private Integer machineRoomId;
    private String machineRoomName;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String latitude;
    private String longitude;


}
