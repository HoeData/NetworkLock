package com.manniu.screen.vo.view;

import lombok.Data;

@Data
public class LockScreenElectronicLockViewVO {

    private Integer id;
    private String address;
    private String serialNumber;
    private String modelName;
    private String equipmentName;
    private Integer networkControlId;
    private Integer networkControlName;

    /**
     * 门状态
     */
    private String doorStatusStr;
    /**
     * 在线状态
     */
    private String onlineStatusStr;
    /**
     * 锁状态
     */
    private String lockStatusStr;
}
