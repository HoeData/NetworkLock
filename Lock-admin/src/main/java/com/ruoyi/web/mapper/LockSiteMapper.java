package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.domain.LockSite;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.domain.vo.LockCommonViewVO;
import java.util.List;

public interface LockSiteMapper extends BaseMapper<LockSite> {


    List<LockCommonViewVO> selectSiteList(LockCommonParamVO lockCommonParamVO);

    int deleteByIds(String[] ids);
}
