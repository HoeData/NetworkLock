package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.web.domain.LockEquipment;
import com.ruoyi.web.domain.LockEquipmentType;
import com.ruoyi.web.domain.LockPortInfo;
import com.ruoyi.web.domain.vo.LockEquipmentAddParamVO;
import com.ruoyi.web.domain.vo.LockEquipmentParamVO;
import com.ruoyi.web.domain.vo.LockEquipmentViewVO;
import com.ruoyi.web.mapper.LockEquipmentMapper;
import com.ruoyi.web.service.ILockEquipmentService;
import com.ruoyi.web.service.ILockPortInfoService;
import com.ruoyi.web.utils.CommonUtils;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class LockEquipmentServiceImpl extends
    ServiceImpl<LockEquipmentMapper, LockEquipment> implements ILockEquipmentService {

    @Resource
    private LockEquipmentMapper lockEquipmentMapper;
    @Resource
    private ILockPortInfoService lockPortInfoService;


    @Override
    public List<LockEquipmentViewVO> selectEquipmentList(LockEquipmentParamVO lockCommonParamVO) {
        return lockEquipmentMapper.selectEquipmentList(lockCommonParamVO);
    }

    @Override
    public int deleteByIds(String[] ids) {
        lockPortInfoService.deleteByEquipmentIds(ids);
        return lockEquipmentMapper.deleteByIds(ids);
    }

    @Override
    public int add(LockEquipmentAddParamVO lockEquipmentAddParamVO) {
        LockEquipment lockEquipment = new LockEquipment();
        BeanUtils.copyProperties(lockEquipmentAddParamVO, lockEquipment);
        save(lockEquipment);
        if (lockEquipment.getPortNumber() > 0) {
            List<LockPortInfo> lockPortInfoList = new ArrayList<>();
            for (int i = 0; i < lockEquipment.getPortNumber(); i++) {
                LockPortInfo lockPortInfo = new LockPortInfo();
                lockPortInfo.setEquipmentId(lockEquipment.getId());
                lockPortInfo.setSerialNumber(i + 1);
                CommonUtils.addCommonParams(lockPortInfo, lockPortInfo.getId());
                lockPortInfoList.add(lockPortInfo);
            }
            lockPortInfoService.saveBatch(lockPortInfoList);
        }
        return 1;
    }

    @Override
    public int update(LockEquipmentAddParamVO lockEquipmentAddParamVO) {
        LockEquipment lockEquipment = new LockEquipment();
        BeanUtils.copyProperties(lockEquipmentAddParamVO, lockEquipment);
        updateById(lockEquipment);
        return 1;
    }

    @Override
    public int setTrust(LockEquipmentAddParamVO lockEquipmentAddParamVO) {
        LockEquipment lockEquipment = new LockEquipment();
        lockEquipment.setId(lockEquipmentAddParamVO.getId());
        lockEquipment.setTrustFlag(lockEquipmentAddParamVO.getTrustFlag());
        return updateById(lockEquipment) ? 1 : 0;
    }

    @Override
    public void judgeName(Integer id, String name, Integer cabinetId) {
        LambdaQueryWrapper<LockEquipment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LockEquipment::getName, name);
        wrapper.eq(LockEquipment::getCabinetId, cabinetId);
        wrapper.eq(LockEquipment::getDelFlag, 0);
        LockEquipment old = getOne(wrapper);
        if (null != old && null == id) {
            throw new ServiceException("同机柜下设备名称已存在");
        }
        if (null != old) {
            if (!old.getId().equals(id)) {
                throw new ServiceException("同机柜下设备名称已存在");
            }
        }
    }

    @Override
    public List<LockEquipment> getAll() {
        LambdaQueryWrapper<LockEquipment> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(LockEquipment::getDelFlag,0);
        return list(wrapper);
    }


}
