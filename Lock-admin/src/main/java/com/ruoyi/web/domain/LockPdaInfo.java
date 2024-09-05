package com.ruoyi.web.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.web.domain.vo.LockEntity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
@TableName("lock_pda_info")
public class LockPdaInfo extends LockEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;
    @NotBlank(message = "唯一标识不能为空")
    private String key;
    @NotNull(message = "类型不能为空")
    private Integer type;
    @NotBlank(message = "描述不能为空")
    private String description;
    private String delFlag;

}
