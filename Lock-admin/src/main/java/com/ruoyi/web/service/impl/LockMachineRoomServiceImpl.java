package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.domain.LockMachineRoom;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.domain.vo.LockCommonViewVO;
import com.ruoyi.web.mapper.LockMachineRoomMapper;
import com.ruoyi.web.service.ILockMachineRoomService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class LockMachineRoomServiceImpl extends
    ServiceImpl<LockMachineRoomMapper, LockMachineRoom> implements ILockMachineRoomService {

    @Resource
    private LockMachineRoomMapper lockMachineRoomMapper;

    @Override
    public List<LockCommonViewVO> selectMachineRoomList(LockCommonParamVO lockCommonParamVO) {
        return lockMachineRoomMapper.selectMachineRoomList(lockCommonParamVO);
    }

    @Override
    public int deleteByIds(String[] ids) {
        return lockMachineRoomMapper.deleteByIds(ids);
    }
}
