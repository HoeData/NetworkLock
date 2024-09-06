package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.web.domain.LockPortInfo;
import com.ruoyi.web.domain.vo.port.LockPortInfoListParamVO;
import com.ruoyi.web.domain.vo.port.LockPortInfoStatisticalQuantityVO;
import com.ruoyi.web.domain.vo.MonitorPortViewVO;
import java.util.List;

public interface ILockPortInfoService extends IService<LockPortInfo> {


    void deleteByEquipmentIds(String[] ids);

    List<LockPortInfo> selectPortInfoList(LockPortInfoListParamVO portInfoListParamVO);

    LockPortInfoStatisticalQuantityVO getStatisticalQuantity();

    String getHexMessageForAddLock(List<LockPortInfo> list);

    String getHexMessageForDelLock(List<LockPortInfo> list);

    List<MonitorPortViewVO> getMonitorPortList();

    List<LockPortInfo> getAll();
}
