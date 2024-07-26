package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.domain.LockCabinet;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.domain.vo.LockCommonViewVO;
import java.util.List;

public interface LockCabinetMapper extends BaseMapper<LockCabinet> {

    List<LockCommonViewVO> selectCabinetList(LockCommonParamVO lockCommonParamVO);

    int deleteByIds(String[] ids);
}
