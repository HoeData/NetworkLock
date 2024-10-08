package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.domain.LockMachineRoom;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.domain.vo.LockCommonViewVO;
import java.util.List;

public interface LockMachineRoomMapper extends BaseMapper<LockMachineRoom> {

    List<LockCommonViewVO> selectMachineRoomList(LockCommonParamVO lockCommonParamVO);

    int deleteByIds(String[] ids);
}
