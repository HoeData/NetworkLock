package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.web.annotation.CompanyScope;
import com.ruoyi.web.domain.LockCabinet;
import com.ruoyi.web.domain.LockMachineRoom;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.domain.vo.LockCommonViewVO;
import com.ruoyi.web.mapper.LockMachineRoomMapper;
import com.ruoyi.web.service.ILockCabinetService;
import com.ruoyi.web.service.ILockMachineRoomService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LockMachineRoomServiceImpl extends
    ServiceImpl<LockMachineRoomMapper, LockMachineRoom> implements ILockMachineRoomService {

    private final LockMachineRoomMapper lockMachineRoomMapper;
    private final ILockCabinetService cabinetService;

    @Override
    @CompanyScope()
    public List<LockCommonViewVO> selectMachineRoomList(LockCommonParamVO lockCommonParamVO) {
        return lockMachineRoomMapper.selectMachineRoomList(lockCommonParamVO);
    }

    @Override
    public int deleteByIds(String[] ids) {
        judgeDelete(ids);
        return lockMachineRoomMapper.deleteByIds(ids);
    }

    private void judgeDelete(String[] ids) {
        for (String id : ids) {
            LambdaQueryWrapper<LockCabinet> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(LockCabinet::getMachineRoomId, id);
            lambdaQueryWrapper.eq(LockCabinet::getDelFlag, 0);
            if (cabinetService.count(lambdaQueryWrapper) > 0) {
                throw new ServiceException("删除机房存在下属机柜,无法删除");
            }
        }
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
    @CompanyScope()
    public List<LockMachineRoom> getAll(LockCommonParamVO lockCommonParamVO) {
        return lockMachineRoomMapper.selectAllList(lockCommonParamVO);
    }
}
