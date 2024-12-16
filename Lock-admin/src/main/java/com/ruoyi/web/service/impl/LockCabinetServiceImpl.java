package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.web.annotation.CompanyScope;
import com.ruoyi.web.domain.LockCabinet;
import com.ruoyi.web.domain.LockEquipment;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.domain.vo.LockCommonViewVO;
import com.ruoyi.web.mapper.LockCabinetMapper;
import com.ruoyi.web.service.ILockCabinetService;
import com.ruoyi.web.service.ILockEquipmentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LockCabinetServiceImpl extends ServiceImpl<LockCabinetMapper, LockCabinet> implements
    ILockCabinetService {

    private final LockCabinetMapper lockCabinetMapper;
    private final ILockEquipmentService lockEquipmentService;

    @Override
    @CompanyScope()
    public List<LockCommonViewVO> selectCabinetList(LockCommonParamVO lockCommonParamVO) {
        return lockCabinetMapper.selectCabinetList(lockCommonParamVO);
    }

    @Override
    public int deleteByIds(String[] ids) {
        judgeDelete(ids);
        return lockCabinetMapper.deleteByIds(ids);
    }

    private void judgeDelete(String[] ids) {
        for (String id : ids) {
            LambdaQueryWrapper<LockEquipment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(LockEquipment::getCabinetId, id);
            lambdaQueryWrapper.eq(LockEquipment::getDelFlag, 0);
            if (lockEquipmentService.count(lambdaQueryWrapper) > 0) {
                throw new ServiceException("删除机柜存在下属设备,无法删除");
            }
        }
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
        if (null != old && !old.getId().equals(lockCabinet.getId())) {
            throw new ServiceException("机柜名称已存在");
        }
    }

    @Override
    @CompanyScope()
    public List<LockCabinet> getAll(LockCommonParamVO lockCommonParamVO) {
        return lockCabinetMapper.selectAllList(lockCommonParamVO);
    }

    @Override
    public void saveOrUpdateForSynchronization(List<LockCabinet> list) {
        saveOrUpdateBatch(list);
    }
}
