package com.manniu.offline.domain;

import lombok.Data;

@Data
public class LockPdaUser extends LockEntity {

    private Integer id;
    private Integer pdaId;
    private String userName;
    private String password;
    private String description;
    private String delFlag;
    private Integer adminFlag;
}
