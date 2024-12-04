package com.manniu.screen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manniu.screen.domain.LockScreenElectronicLock;
import com.manniu.screen.mapper.LockScreenElectronicLockMapper;
import com.manniu.screen.service.ILockScreenElectronicLockService;
import com.manniu.screen.vo.param.LockScreenElectronicLockPageParamVO;
import com.manniu.screen.vo.view.LockScreenElectronicLockViewVO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LockScreenElectronicLockServiceImpl extends
    ServiceImpl<LockScreenElectronicLockMapper, LockScreenElectronicLock> implements
    ILockScreenElectronicLockService {

    private final LockScreenElectronicLockMapper lockScreenElectronicLockMapper;

    @Override
    public List<LockScreenElectronicLockViewVO> getElectronicLockList(
        LockScreenElectronicLockPageParamVO pageParamVO) {
        List<LockScreenElectronicLockViewVO> list = lockScreenElectronicLockMapper.selectElectronicLockList(pageParamVO);
        return null;
    }
}
