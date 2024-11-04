package com.ruoyi.web.domain.vo.importvo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ImportEquipmentTypeVO {

    @ExcelProperty("设备类型名称*")
    private String equipmentTypeName;
    @ExcelProperty("设备类型描述")
    private String equipmentTypeDescription;
    @ExcelProperty("错误原因")
    private String errorMsg;
}
