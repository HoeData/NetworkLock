package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.domain.LockEquipment;
import com.ruoyi.web.domain.vo.LockEquipmentParamVO;
import com.ruoyi.web.domain.vo.LockEquipmentViewVO;
import java.util.List;

public interface LockEquipmentMapper extends BaseMapper<LockEquipment> {

    List<LockEquipmentViewVO> selectEquipmentList(LockEquipmentParamVO lockCommonParamVO);

    int deleteByIds(String[] ids);

    int selectConsoleTotal();

}
