package com.ruoyi.web.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.web.domain.vo.LockEntity;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.apache.commons.compress.utils.Lists;

@TableName(value = "lock_company")
@Data
public class LockCompany extends LockEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;
    @NotBlank(message = "公司名称不能为空")
    private String name;
    private String description;
    private String delFlag;

    private Integer parentId;
    private String path;
    @TableField(exist = false)
    private List<LockCompany> children = Lists.newArrayList();
}
