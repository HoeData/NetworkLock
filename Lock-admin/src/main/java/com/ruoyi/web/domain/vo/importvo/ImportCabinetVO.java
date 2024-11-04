package com.ruoyi.web.domain.vo.importvo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ImportCabinetVO {

    @ExcelProperty("机柜名称*")
    private String cabinetName;
    @ExcelProperty("机柜描述")
    private String cabinetDescription;
    @ExcelProperty("所属机房*")
    private String machineRoomName;
    @ExcelIgnore
    private Integer machineRoomId;
    @ExcelProperty("错误原因")
    private String errorMsg;
}
