package com.manniu.offline.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;

@Data
public class LockPdaDataSynchronizationInfo {

    private Integer id;
    private Integer pdaId;
    private Integer status;
    private String delFlag;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private Integer type;


}
