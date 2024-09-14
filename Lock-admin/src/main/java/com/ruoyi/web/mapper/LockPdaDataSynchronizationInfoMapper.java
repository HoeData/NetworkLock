package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.domain.LockPdaDataSynchronizationInfo;
import com.ruoyi.web.domain.vo.pda.LockPdaDataSynchronizationInfoPageParamVO;
import com.ruoyi.web.domain.vo.pda.LockPdaDataSynchronizationInfoViewVO;
import java.util.List;

public interface LockPdaDataSynchronizationInfoMapper extends
    BaseMapper<LockPdaDataSynchronizationInfo> {

    LockPdaDataSynchronizationInfo getLastByDeviceId(String deviceId);

    List<LockPdaDataSynchronizationInfoViewVO> selectSynchronizationInfoList(
        LockPdaDataSynchronizationInfoPageParamVO viewVO);
}
