package com.ruoyi.web.domain.vo.importvo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ImportMachineRoomVO {
    @ExcelProperty("机房名称*")
    private String machineRoomName;
    @ExcelProperty("机房描述")
    private String machineRoomDescription;
    @ExcelProperty("所属站点*")
    private String siteName;
    @ExcelProperty("错误原因")
    private String errorMsg;
    @ExcelIgnore
    private Integer siteId;
}
