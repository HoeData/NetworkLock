package com.ruoyi.web.domain.vo.pda;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;

@Data
@ExcelIgnoreUnannotated
public class LockPdaDataSynchronizationInfoViewVO {

    @ExcelProperty("PDA/钥匙")
    private String pdaKey;
    @ExcelProperty("PDA/钥匙描述")
    private String pdaDescription;
    @ExcelProperty("同步描述")
    private String statusDesc;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelProperty("创建时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private Integer id;
    private Integer pdaId;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private Integer type;


}
