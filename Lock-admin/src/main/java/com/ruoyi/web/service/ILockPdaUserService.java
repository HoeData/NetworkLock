package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.web.domain.LockPdaUser;
import com.ruoyi.web.domain.vo.pda.LockPdaUserPageParamVO;
import java.util.List;

public interface ILockPdaUserService extends IService<LockPdaUser> {

    List<LockPdaUser> getPdaUserList(LockPdaUserPageParamVO vo);

    void judgeUserName(LockPdaUser pdaUser);

    void judgeAdminFlag(LockPdaUser pdaUser);


    int deleteByIds(String[] ids);

    void judgeWhetherEdit(Integer pdaId);

    int delByPdaId(String[] ids);
}
