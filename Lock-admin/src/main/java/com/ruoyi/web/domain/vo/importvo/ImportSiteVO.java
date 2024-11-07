package com.ruoyi.web.domain.vo.importvo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ImportSiteVO {

    @ExcelProperty("站点名称*")
    private String siteName;
    @ExcelProperty("站点描述")
    private String siteDescription;
    @ExcelProperty("经度")
    private String longitude;
    @ExcelProperty("纬度")
    private String latitude;
    @ExcelProperty("所属公司*")
    private String companyName;
    @ExcelProperty("错误原因")
    private String errorMsg;
    @ExcelIgnore
    private Integer companyId;
}
