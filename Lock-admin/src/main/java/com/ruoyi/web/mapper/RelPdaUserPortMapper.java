package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.domain.LockPortInfo;
import com.ruoyi.web.domain.RelPdaUserPort;
import com.ruoyi.web.domain.vo.pda.RelPdaUserPortParamVO;
import com.ruoyi.web.domain.vo.pda.RelPdaUserPortViewVO;
import java.util.List;

public interface RelPdaUserPortMapper extends BaseMapper<RelPdaUserPort> {

    List<RelPdaUserPortViewVO> selectAllList(RelPdaUserPortParamVO vo);

    List<RelPdaUserPortViewVO> selectAuthorizationList(RelPdaUserPortParamVO vo);
    List<LockPortInfo> selectAuthorizationPortList(Integer pdaUserId);

    void deleteByPdaUserId(Integer pdaUserId);
}
