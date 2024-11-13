package com.manniu.offline.domain;

import lombok.Data;

@Data
public class LockCompany extends LockEntity {

    private Integer id;
    private String name;
    private String description;
    private String delFlag;

    private Integer parentId;
    private String path;
}
