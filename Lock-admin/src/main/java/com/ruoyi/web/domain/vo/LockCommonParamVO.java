package com.ruoyi.web.domain.vo;



public class LockCommonParamVO extends PageVO{

    private String name;
    private String description;
    private Integer companyId;
    private Integer companyName;
    private Integer siteId;
    private String siteName;

    private Integer machineRoomId;
    private String machineRoomName;

    public Integer getMachineRoomId() {
        return machineRoomId;
    }

    public void setMachineRoomId(Integer machineRoomId) {
        this.machineRoomId = machineRoomId;
    }

    public String getMachineRoomName() {
        return machineRoomName;
    }

    public void setMachineRoomName(String machineRoomName) {
        this.machineRoomName = machineRoomName;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getCompanyName() {
        return companyName;
    }

    public void setCompanyName(Integer companyName) {
        this.companyName = companyName;
    }
}
