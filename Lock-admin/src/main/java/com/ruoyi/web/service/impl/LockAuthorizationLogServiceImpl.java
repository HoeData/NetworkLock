package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.annotation.CompanyScope;
import com.ruoyi.web.domain.LockAuthorizationLog;
import com.ruoyi.web.domain.vo.pda.UnlockPageParamVO;
import com.ruoyi.web.domain.vo.pda.UnlockViewVO;
import com.ruoyi.web.mapper.LockAuthorizationLogMapper;
import com.ruoyi.web.service.ILockAuthorizationLogService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LockAuthorizationLogServiceImpl extends
    ServiceImpl<LockAuthorizationLogMapper, LockAuthorizationLog> implements
    ILockAuthorizationLogService {

    private final LockAuthorizationLogMapper lockAuthorizationLogMapper;

    @Override
    @CompanyScope()
    public List<UnlockViewVO> getAllList(UnlockPageParamVO vo) {
        return lockAuthorizationLogMapper.selectAllList(vo);
    }
}
