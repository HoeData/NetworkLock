package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.domain.LockPdaInfo;
import com.ruoyi.web.domain.LockPdaUser;
import com.ruoyi.web.domain.vo.pda.LockPdaUserPageParamVO;
import java.util.List;

public interface LockPdaUserMapper extends BaseMapper<LockPdaUser> {

    /**
     * 获取pda用户列表
     *
     * @param vo vo
     * @return {@link List }<{@link LockPdaInfo }>
     */
    List<LockPdaUser> selectPdaUserList(LockPdaUserPageParamVO vo);

    int deleteByIds(String[] ids);

    int delByPdaId(String[] ids);
}
