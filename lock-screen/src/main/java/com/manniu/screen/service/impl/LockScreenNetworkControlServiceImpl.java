package com.manniu.screen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manniu.screen.domain.LockScreenNetworkControl;
import com.manniu.screen.mapper.LockScreenNetworkControlMapper;
import com.manniu.screen.service.ILockScreenNetworkControlService;
import com.manniu.screen.vo.param.LockScreenNetworkControlPageParamVO;
import com.sun.jna.platform.win32.Winspool.PRINTER_INFO_1;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LockScreenNetworkControlServiceImpl extends
    ServiceImpl<LockScreenNetworkControlMapper, LockScreenNetworkControl> implements
    ILockScreenNetworkControlService {
private final LockScreenNetworkControlMapper lockScreenNetworkControlMapper;
    @Override
    public List<LockScreenNetworkControl> getNetworkControlList(
        LockScreenNetworkControlPageParamVO pageParamVO) {
        return lockScreenNetworkControlMapper.selectNetworkControlList(pageParamVO);
    }
}
