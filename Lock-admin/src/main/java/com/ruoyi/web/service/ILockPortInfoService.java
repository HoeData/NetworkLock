package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.web.domain.LockPortInfo;
import com.ruoyi.web.domain.vo.LockPortInfoListParamVO;
import com.ruoyi.web.domain.vo.LockPortInfoStatisticalQuantityVO;
import java.util.List;

public interface ILockPortInfoService extends IService<LockPortInfo> {


    void deleteByEquipmentIds(String[] ids);

    List<LockPortInfo> selectPortInfoList(LockPortInfoListParamVO portInfoListParamVO);

    LockPortInfoStatisticalQuantityVO getStatisticalQuantity();
}
