package com.manniu.screen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manniu.screen.domain.LockScreenElectronicLock;
import com.manniu.screen.vo.param.LockScreenElectronicLockPageParamVO;
import com.manniu.screen.vo.view.LockScreenElectronicLockViewVO;
import java.util.List;

public interface LockScreenElectronicLockMapper extends BaseMapper<LockScreenElectronicLock> {

    List<LockScreenElectronicLockViewVO> selectElectronicLockList(
        LockScreenElectronicLockPageParamVO pageParamVO);
}
