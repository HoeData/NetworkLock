package com.manniu.screen.vo;

import javax.validation.constraints.NotBlank;

public class CommonVO {

    @NotBlank(message = "ip不能为空")
    private String ip;
    @NotBlank(message = "设备不能为空")
    private String deviceId;
    @NotBlank(message = "锁ID不能为空")
    private Integer lockId;
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getLockId() {
        return lockId;
    }

    public void setLockId(Integer lockId) {
        this.lockId = lockId;
    }
}
