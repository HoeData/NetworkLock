package com.manniu.offline.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;

@Data
public class RelPdaUserPort {

    private Integer id;
    private Integer pdaUserId;
    private Integer portInfoId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date validityPeriod;
    private Date createTime;
    private Integer serialNumber;
    private String lockSerialNumber;

}
