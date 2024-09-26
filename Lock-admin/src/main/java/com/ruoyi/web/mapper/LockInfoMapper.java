package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.domain.LockInfo;
import com.ruoyi.web.domain.vo.LockInfoPageParamVO;
import com.ruoyi.web.domain.vo.LockInfoViewVO;
import java.util.List;

public interface LockInfoMapper extends BaseMapper<LockInfo> {

    List<LockInfoViewVO> selectAllList(LockInfoPageParamVO vo);
}
