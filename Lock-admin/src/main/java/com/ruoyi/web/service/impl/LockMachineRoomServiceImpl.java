package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.web.domain.LockMachineRoom;
import com.ruoyi.web.domain.LockSite;
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

    @Override
    public void judgeName(LockMachineRoom lockMachineRoom) {
        LambdaQueryWrapper<LockMachineRoom> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LockMachineRoom::getName, lockMachineRoom.getName());
        wrapper.eq(LockMachineRoom::getDelFlag, 0);
        wrapper.eq(LockMachineRoom::getSiteId, lockMachineRoom.getSiteId());
        LockMachineRoom old = getOne(wrapper);
        if (null != old && null == lockMachineRoom.getId()) {
            throw new ServiceException("机房名称已存在");
        }
        if (null != old) {
            if (!old.getId().equals(lockMachineRoom.getId())) {
                throw new ServiceException("机房名称已存在");
            }
        }
    }

    @Override
    public List<LockMachineRoom> getAll() {
        LambdaQueryWrapper<LockMachineRoom> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(LockMachineRoom::getDelFlag,0);
        return list(wrapper);
    }
}
