package com.ruoyi.web.domain.vo;


import lombok.Data;

@Data
public class LockCommonParamVO extends PageVO{

    private String name;
    private String description;
    private Integer companyId;
    private Integer companyName;
    private Integer siteId;
    private String siteName;

    private Integer machineRoomId;
    private String machineRoomName;

}
