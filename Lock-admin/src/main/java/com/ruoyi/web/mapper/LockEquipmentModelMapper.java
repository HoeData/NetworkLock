package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.domain.LockEquipmentModel;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import java.util.List;

public interface LockEquipmentModelMapper extends BaseMapper<LockEquipmentModel> {

    List<LockEquipmentModel> selectEquipmentModelList(LockCommonParamVO lockCommonParamVO);

    int deleteByIds(String[] ids);
}
