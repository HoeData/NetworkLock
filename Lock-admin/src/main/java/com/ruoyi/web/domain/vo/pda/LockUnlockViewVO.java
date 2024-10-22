package com.ruoyi.web.domain.vo.pda;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@ExcelIgnoreUnannotated
public class LockUnlockViewVO {

    private String id;
    private Integer pdaUserId;
    @ExcelProperty("用户名")
    private String pdaUserName;
    @ExcelProperty("设备")
    private String equipmentName;
    @ExcelProperty("端口号")
    private String portIndex;
    private Integer portId;
    @ExcelProperty("锁编号")
    private String lockSerialNumber;
    private Integer status;
    @ExcelProperty("状态")
    private String statusStr;
    @ExcelProperty("描述")
    private String errorMsg;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelProperty("创建时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
