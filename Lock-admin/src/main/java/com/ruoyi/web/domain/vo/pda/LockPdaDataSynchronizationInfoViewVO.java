package com.ruoyi.web.domain.vo.pda;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.page.PageDomain;
import java.util.Date;
import lombok.Data;

@Data
public class LockPdaDataSynchronizationInfoViewVO {

    private Integer id;
    private Integer pdaId;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private Integer type;
    private String statusDesc;
    private String pdaKey;

    private String pdaDescription;
}
