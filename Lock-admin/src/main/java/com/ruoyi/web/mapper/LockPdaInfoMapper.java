package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.domain.vo.pda.LockPadPageParamVO;
import com.ruoyi.web.domain.LockPdaInfo;
import java.util.List;

public interface LockPdaInfoMapper extends BaseMapper<LockPdaInfo> {

    List<LockPdaInfo> selectPdaInfoList(LockPadPageParamVO padPageParamVO);

    int deleteByIds(String[] ids);
}
