package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.web.domain.LockUnlockLog;
import com.ruoyi.web.domain.vo.pda.LockUnlockViewVO;
import com.ruoyi.web.domain.vo.pda.UnlockPageParamVO;
import com.ruoyi.web.domain.vo.port.LockUnlockHexMessageVO;
import java.util.List;

public interface ILockUnlockLogService extends IService<LockUnlockLog> {

    List<LockUnlockViewVO> selectUnlockList(UnlockPageParamVO pageVO);
    /**
     * 同步密钥解锁日志 异步执行--注意没有指定自定义的线程池用的是spring自带的
     *
     * @param lockUnlockHexMessageVO 报文VO
     */
    void syncKeyUnlockLog(LockUnlockHexMessageVO lockUnlockHexMessageVO);
}
