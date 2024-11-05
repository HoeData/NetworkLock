package com.manniu.offline.domain;


import com.google.common.collect.Lists;
import java.util.List;
import lombok.Data;

@Data
public class LockCompany extends LockEntity {

    private Integer id;
    private String name;
    private String description;
    private String delFlag;

    private Integer parentId;
    private String path;
    private List<LockCompany> children = Lists.newArrayList();
}
