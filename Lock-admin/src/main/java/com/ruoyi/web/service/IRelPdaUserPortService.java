package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.web.domain.RelPdaUserPort;
import com.ruoyi.web.domain.vo.pda.RelPdaUserPortParamVO;
import com.ruoyi.web.domain.vo.pda.RelPdaUserPortViewVO;
import java.util.List;

public interface IRelPdaUserPortService extends IService<RelPdaUserPort> {

    List<RelPdaUserPortViewVO> getAllList(RelPdaUserPortParamVO vo);

    List<RelPdaUserPortViewVO> getAuthorizationList(RelPdaUserPortParamVO vo);

    int saveAuthorization(List<RelPdaUserPort> list);
}
