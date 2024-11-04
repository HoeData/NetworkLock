package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.web.domain.LockMachineRoom;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.domain.vo.LockCommonViewVO;
import java.util.List;

public interface ILockMachineRoomService extends IService<LockMachineRoom> {


    List<LockCommonViewVO> selectMachineRoomList(LockCommonParamVO lockCommonParamVO);

    int deleteByIds(String[] ids);

    void judgeName(LockMachineRoom lockMachineRoom);

    List<LockMachineRoom> getAll(LockCommonParamVO lockCommonParamVO);
}
