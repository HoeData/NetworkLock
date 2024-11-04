package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.domain.LockEquipment;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.domain.vo.equipment.LockEquipmentParamVO;
import com.ruoyi.web.domain.vo.equipment.LockEquipmentViewVO;
import java.util.List;

public interface LockEquipmentMapper extends BaseMapper<LockEquipment> {

    List<LockEquipmentViewVO> selectEquipmentList(LockEquipmentParamVO lockEquipmentParamVO);

    int deleteByIds(String[] ids);

    int selectConsoleTotal(LockCommonParamVO vo);

    int removeActiveDefenseByIds(String[] ids);

    List<LockEquipment> selectAllList(LockEquipmentParamVO lockEquipmentParamVO);
}
