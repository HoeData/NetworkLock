package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.web.domain.LockCabinet;
import com.ruoyi.web.domain.LockMachineRoom;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.domain.vo.LockCommonViewVO;
import com.ruoyi.web.mapper.LockCabinetMapper;
import com.ruoyi.web.service.ILockCabinetService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class LockCabinetServiceImpl extends
    ServiceImpl<LockCabinetMapper, LockCabinet> implements ILockCabinetService {

    @Resource
    private LockCabinetMapper lockCabinetMapper;

    @Override
    public List<LockCommonViewVO> selectCabinetList(LockCommonParamVO lockCommonParamVO) {
        return lockCabinetMapper.selectCabinetList(lockCommonParamVO);
    }

    @Override
    public int deleteByIds(String[] ids) {
        return lockCabinetMapper.deleteByIds(ids);
    }

    @Override
    public void judgeName(LockCabinet lockCabinet) {
        LambdaQueryWrapper<LockCabinet> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LockCabinet::getName, lockCabinet.getName());
        wrapper.eq(LockCabinet::getDelFlag, 0);
        wrapper.eq(LockCabinet::getMachineRoomId, lockCabinet.getMachineRoomId());
        LockCabinet old = getOne(wrapper);
        if (null != old && null == lockCabinet.getId()) {
            throw new ServiceException("机柜名称已存在");
        }
        if (null != old) {
            if (!old.getId().equals(lockCabinet.getId())) {
                throw new ServiceException("机柜名称已存在");
            }
        }
    }

    @Override
    public List<LockCabinet> getAll() {
        LambdaQueryWrapper<LockCabinet> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(LockCabinet::getDelFlag,0);
        return list(wrapper);
    }
}
