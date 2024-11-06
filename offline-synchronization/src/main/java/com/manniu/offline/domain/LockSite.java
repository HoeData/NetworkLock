package com.manniu.offline.domain;


import lombok.Data;

@Data
public class LockSite extends LockEntity {

    private Integer id;
    private Integer companyId;
    private String name;
    private String description;
    private String delFlag;
    private String latitude;
    private String longitude;

}
