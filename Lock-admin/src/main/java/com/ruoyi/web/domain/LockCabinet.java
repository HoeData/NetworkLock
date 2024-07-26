package com.ruoyi.web.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.web.domain.vo.LockEntity;

@TableName(value = "lock_cabinet")
public class LockCabinet extends LockEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer machineRoomId;

    private String name;
    private String description;
    private String delFlag;

    public Integer getMachineRoomId() {
        return machineRoomId;
    }

    public void setMachineRoomId(Integer machineRoomId) {
        this.machineRoomId = machineRoomId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
}
