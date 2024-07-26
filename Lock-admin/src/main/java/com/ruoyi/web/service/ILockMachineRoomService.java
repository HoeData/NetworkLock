package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.web.domain.LockMachineRoom;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import java.util.List;

public interface ILockMachineRoomService extends IService<LockMachineRoom> {


    List<LockMachineRoom> selectMachineRoomList(LockCommonParamVO lockCommonParamVO);

    int deleteByIds(String[] ids);
}
