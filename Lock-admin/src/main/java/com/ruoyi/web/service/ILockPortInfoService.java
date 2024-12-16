package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.web.domain.LockPortInfo;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.domain.vo.MonitorPortViewVO;
import com.ruoyi.web.domain.vo.port.LockPortInfoListParamVO;
import com.ruoyi.web.domain.vo.port.LockPortInfoStatisticalQuantityVO;
import java.util.List;

public interface ILockPortInfoService extends IService<LockPortInfo> {


    void deleteByEquipmentIds(String[] ids);

    List<LockPortInfo> selectPortInfoList(LockPortInfoListParamVO portInfoListParamVO);

    LockPortInfoStatisticalQuantityVO getStatisticalQuantity(LockCommonParamVO vo);

    List<MonitorPortViewVO> getMonitorPortList();

    List<LockPortInfo> getAll(LockPortInfoListParamVO portInfoListParamVO);
    void judgeUserCode(Integer id,String userCode);
    void saveOrUpdateForSynchronization(List<LockPortInfo> list);
}
