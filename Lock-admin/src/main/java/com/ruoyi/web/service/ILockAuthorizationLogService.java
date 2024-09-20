package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.web.domain.LockAuthorizationLog;
import com.ruoyi.web.domain.vo.pda.UnlockPageParamVO;
import com.ruoyi.web.domain.vo.pda.UnlockViewVO;
import java.util.List;

public interface ILockAuthorizationLogService extends IService<LockAuthorizationLog> {

    List<UnlockViewVO> getAllList(UnlockPageParamVO vo);
}
