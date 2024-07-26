package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.domain.LockDept;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.domain.vo.LockCommonViewVO;
import java.util.List;

public interface LockDeptMapper extends BaseMapper<LockDept> {


    List<LockCommonViewVO> selectDeptList(LockCommonParamVO lockCommonParamVO);

    int deleteByIds(String[] ids);
}
