package com.ruoyi.web.domain.vo.pda;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;

@Data
@ExcelIgnoreUnannotated
public class UnlockViewVO {

    private Integer id;
    private Integer pdaUserId;
    private String pdaDescription;
    @ExcelProperty("用户名")
    private String pdaUserName;
    private Integer successFlag;
    @ExcelProperty("状态")
    private String successFlagStr;
    private String errorMsg;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelProperty("创建时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
