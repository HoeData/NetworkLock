package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.domain.LockWarnInfo;
import com.ruoyi.web.domain.vo.LockEquipmentParamVO;
import com.ruoyi.web.domain.vo.LockWarnInfoViewVO;
import java.util.List;

public interface LockWarnInfoMapper extends BaseMapper<LockWarnInfo> {

    LockWarnInfo selectLastNoConfirmByPortInfoId(Integer portInfoId);

    LockWarnInfoViewVO selectTheLastWarn();

    List<LockWarnInfoViewVO> selectWarnInfoList(LockEquipmentParamVO lockEquipmentParamVO);
}
