package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.domain.LockWarnInfo;
import com.ruoyi.web.domain.vo.LockEquipmentParamVO;
import com.ruoyi.web.domain.vo.LockWarnInfoViewVO;
import com.ruoyi.web.domain.vo.index.Recent12MonthsStatisticsVO;
import java.time.LocalDate;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface LockWarnInfoMapper extends BaseMapper<LockWarnInfo> {

    LockWarnInfo selectLastNoConfirmByPortInfoId(Integer portInfoId);

    LockWarnInfoViewVO selectTheLastWarn();

    List<LockWarnInfoViewVO> selectWarnInfoList(LockEquipmentParamVO lockEquipmentParamVO);

    Recent12MonthsStatisticsVO selectTotalByStartAndEndDate(@Param("startDate") LocalDate startDate, @Param("endDate")LocalDate endDate);
}
