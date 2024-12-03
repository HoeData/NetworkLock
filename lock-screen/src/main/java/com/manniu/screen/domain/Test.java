package com.manniu.screen.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName(value = "test")
@Data
public class Test {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;

}
