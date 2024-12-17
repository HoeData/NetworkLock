package com.manniu.screen.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manniu.screen.domain.LockScreenUserLockKey;
import com.manniu.screen.vo.param.LockUserKeyPageParamVO;
import com.manniu.screen.vo.view.LockUserKeyViewVO;
import java.util.List;

public interface LockScreenUserLockKeyMapper extends BaseMapper<LockScreenUserLockKey> {

    List<LockUserKeyViewVO> selectUserLockKeyList(LockUserKeyPageParamVO pageParamVO);
    int deleteByDataId(int id);
}
