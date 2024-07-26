package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.web.domain.LockSite;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.domain.vo.LockCommonViewVO;
import java.util.List;

public interface ILockSiteService extends IService<LockSite> {

    List<LockCommonViewVO> selectSiteList(LockCommonParamVO lockCommonParamVO);

    int deleteByIds(String[] ids);
}
