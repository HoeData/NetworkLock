package com.manniu.offline.domain;


import lombok.Data;

@Data
public class LockDept extends LockEntity {
    private Integer id;
    private Integer companyId;
    private String name;
    private String description;
    private String delFlag;
}
