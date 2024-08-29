package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.web.domain.LockWarnInfo;
import com.ruoyi.web.domain.vo.LockEquipmentParamVO;
import com.ruoyi.web.domain.vo.LockWarnInfoViewVO;
import java.util.List;

public interface ILockWarnInfoService extends IService<LockWarnInfo> {

    LockWarnInfo getLastNoConfirmByPortInfoId(Integer portInfoId);

    LockWarnInfoViewVO getTheLastWarn();

    List<LockWarnInfoViewVO> selectWarnInfoList(LockEquipmentParamVO lockEquipmentParamVO);
}
