package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.domain.LockMachineRoom;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import java.util.List;

public interface LockMachineRoomMapper extends BaseMapper<LockMachineRoom> {

    List<LockMachineRoom> selectMachineRoomList(LockCommonParamVO lockCommonParamVO);

    int deleteByIds(String[] ids);
}
