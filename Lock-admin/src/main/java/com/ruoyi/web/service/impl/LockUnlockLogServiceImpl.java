package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.annotation.CompanyScope;
import com.ruoyi.web.domain.LockUnlockLog;
import com.ruoyi.web.domain.vo.PageVO;
import com.ruoyi.web.domain.vo.pda.LockUnlockViewVO;
import com.ruoyi.web.domain.vo.pda.UnlockPageParamVO;
import com.ruoyi.web.mapper.LockUnlockLogMapper;
import com.ruoyi.web.service.ILockUnlockLogService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LockUnlockLogServiceImpl extends
    ServiceImpl<LockUnlockLogMapper, LockUnlockLog> implements ILockUnlockLogService {

    private final LockUnlockLogMapper unlockLogMapper;

    @Override
    @CompanyScope(companyAlias = "company")
    public List<LockUnlockViewVO> selectUnlockList(UnlockPageParamVO pageVO) {
        return unlockLogMapper.selectUnlockList(pageVO);
    }
}
