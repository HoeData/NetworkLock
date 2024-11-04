package com.ruoyi.web.domain.vo.importvo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ImportDeptVO {
    @ExcelProperty("部门名称*")
    private String deptName;
    @ExcelProperty("部门描述")
    private String deptDescription;
    @ExcelProperty("所属公司*")
    private String companyName;
    @ExcelProperty("错误原因")
    private String errorMsg;
    @ExcelIgnore
    private Integer companyId;
}
