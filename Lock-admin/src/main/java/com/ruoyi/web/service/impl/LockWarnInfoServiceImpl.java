package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.domain.LockWarnInfo;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.domain.vo.equipment.LockEquipmentParamVO;
import com.ruoyi.web.domain.vo.LockWarnInfoViewVO;
import com.ruoyi.web.mapper.LockWarnInfoMapper;
import com.ruoyi.web.service.ILockWarnInfoService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LockWarnInfoServiceImpl extends
    ServiceImpl<LockWarnInfoMapper, LockWarnInfo> implements
    ILockWarnInfoService {

    private final LockWarnInfoMapper lockWarnInfoMapper;

    @Override
    public LockWarnInfo getLastNoConfirmByPortInfoId(Integer portInfoId) {
        return lockWarnInfoMapper.selectLastNoConfirmByPortInfoId(portInfoId);
    }

    @Override
    public LockWarnInfoViewVO getTheLastWarn() {
        return lockWarnInfoMapper.selectTheLastWarn(new LockCommonParamVO());
    }

    @Override
    public List<LockWarnInfoViewVO> selectWarnInfoList(LockEquipmentParamVO lockEquipmentParamVO) {
        return lockWarnInfoMapper.selectWarnInfoList(lockEquipmentParamVO);
    }
}
