package com.manniu.screen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manniu.screen.domain.LockScreenUnlock;
import com.manniu.screen.vo.param.LockScreenUnlockPageParamVO;
import com.manniu.screen.vo.view.LockScreenUnlockVIewVO;
import java.util.List;

public interface LockScreenUnlockMapper extends BaseMapper<LockScreenUnlock> {

    List<LockScreenUnlockVIewVO> selectUnlockList(LockScreenUnlockPageParamVO pageParamVO);
}
