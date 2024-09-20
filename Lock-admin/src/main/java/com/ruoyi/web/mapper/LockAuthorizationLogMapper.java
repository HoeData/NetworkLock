package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.domain.LockAuthorizationLog;
import com.ruoyi.web.domain.vo.pda.UnlockPageParamVO;
import com.ruoyi.web.domain.vo.pda.UnlockViewVO;
import java.util.List;

public interface LockAuthorizationLogMapper extends BaseMapper<LockAuthorizationLog> {

    List<UnlockViewVO> selectAllList(UnlockPageParamVO vo);
}
