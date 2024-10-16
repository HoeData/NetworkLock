package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.web.domain.RelPdaUserPort;
import com.ruoyi.web.domain.vo.pda.RelPdaUserPortParamVO;
import com.ruoyi.web.domain.vo.pda.RelPdaUserPortViewVO;
import java.util.List;
import java.util.Map;

public interface IRelPdaUserPortService extends IService<RelPdaUserPort> {

    List<RelPdaUserPortViewVO> getAllList(RelPdaUserPortParamVO vo);

    List<RelPdaUserPortViewVO> getAuthorizationList(RelPdaUserPortParamVO vo);

    int saveAuthorization(List<RelPdaUserPort> list);

    Map<String,Object> getHexMessageForAddLock(List<RelPdaUserPort> relPdaUserPortList);

    String getHexMessageForDelLock(String[] ids);
}
