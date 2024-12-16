package com.manniu.screen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manniu.screen.config.LockScreenCache;
import com.manniu.screen.domain.LockScreenUnlock;
import com.manniu.screen.mapper.LockScreenUnlockMapper;
import com.manniu.screen.service.ILockScreenUnlockService;
import com.manniu.screen.vo.param.LockScreenUnlockPageParamVO;
import com.manniu.screen.vo.view.LockScreenUnlockVIewVO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ILockScreenUnlockServiceImpl extends
    ServiceImpl<LockScreenUnlockMapper, LockScreenUnlock> implements ILockScreenUnlockService {

    private final LockScreenUnlockMapper unlockMapper;

    @Override
    public List<LockScreenUnlockVIewVO> getUnlockList(LockScreenUnlockPageParamVO pageParamVO) {
        List<LockScreenUnlockVIewVO> list = unlockMapper.selectUnlockList(pageParamVO);
        list.forEach(item -> item.setType(
            LockScreenCache.codeMap.getOrDefault(item.getType(), item.getType())));
        return list;
    }
}
