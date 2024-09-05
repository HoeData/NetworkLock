package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.web.domain.LockPortInfo;
import com.ruoyi.web.domain.RelPdaUserPort;
import com.ruoyi.web.domain.vo.RelPdaUserPortParamVO;
import com.ruoyi.web.domain.vo.RelPdaUserPortViewVO;
import com.ruoyi.web.domain.vo.RelPortViewVO;
import java.util.List;

public interface RelPdaUserPortMapper extends BaseMapper<RelPdaUserPort> {

    List<RelPdaUserPortViewVO> selectAllList(RelPdaUserPortParamVO vo);

    List<RelPdaUserPortViewVO> selectAuthorizationList(RelPdaUserPortParamVO vo);
    List<LockPortInfo> selectAuthorizationPortList(Integer pdaUserId);

    void deleteByPdaUserId(Integer pdaUserId);
}
