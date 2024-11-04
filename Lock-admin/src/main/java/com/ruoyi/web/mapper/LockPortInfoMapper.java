package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.domain.LockPortInfo;
import com.ruoyi.web.domain.RelPdaUserPort;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.domain.vo.port.LockPortInfoListParamVO;
import com.ruoyi.web.domain.vo.MonitorPortViewVO;
import com.ruoyi.web.domain.vo.index.PortStatisticsVO;
import java.util.List;

public interface LockPortInfoMapper extends BaseMapper<LockPortInfo> {


    void deleteByEquipmentIds(String[] ids);

    List<LockPortInfo> selectPortInfoList(LockPortInfoListParamVO portInfoListParamVO);

    int selectPortTotal(LockCommonParamVO vo);

    int selectLockPortTotal(LockCommonParamVO vo);

    int selectUseTotal(LockCommonParamVO vo);

    int selectNoUseTotal(LockCommonParamVO vo);

    List<MonitorPortViewVO> selectonitorPortList();

    List<PortStatisticsVO> selectPortStatisticsVOList();

    Integer selectIdleTotal(LockCommonParamVO vo);

    List<LockPortInfo> selectListByEquipmentIds(List<Integer> equipmentIdList);

    List<LockPortInfo> selectAllList(LockPortInfoListParamVO portInfoListParamVO);
}
