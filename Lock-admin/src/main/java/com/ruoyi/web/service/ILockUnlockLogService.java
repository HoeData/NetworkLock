package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.web.domain.LockUnlockLog;
import com.ruoyi.web.domain.vo.PageVO;
import com.ruoyi.web.domain.vo.pda.LockUnlockViewVO;
import java.util.List;

public interface ILockUnlockLogService extends IService<LockUnlockLog> {

    List<LockUnlockViewVO> selectUnlockList(PageVO pageVO);
}
