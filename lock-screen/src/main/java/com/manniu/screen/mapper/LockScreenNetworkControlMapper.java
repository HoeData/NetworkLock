package com.manniu.screen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manniu.screen.domain.LockScreenNetworkControl;
import com.manniu.screen.vo.param.LockScreenNetworkControlPageParamVO;
import java.util.List;

public interface LockScreenNetworkControlMapper extends BaseMapper<LockScreenNetworkControl> {

    List<LockScreenNetworkControl> selectNetworkControlList(LockScreenNetworkControlPageParamVO pageParamVO);
}
