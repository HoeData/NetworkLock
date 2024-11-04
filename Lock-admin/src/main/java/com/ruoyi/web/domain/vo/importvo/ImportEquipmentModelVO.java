package com.ruoyi.web.domain.vo.importvo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ImportEquipmentModelVO {

    @ExcelProperty("设备型号名称*")
    private String equipmentModelName;
    @ExcelProperty("设备型号描述")
    private String equipmentModelDescription;
    @ExcelProperty("错误原因")
    private String errorMsg;
}
