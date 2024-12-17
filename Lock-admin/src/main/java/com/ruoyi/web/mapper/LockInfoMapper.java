package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.domain.LockInfo;
import com.ruoyi.web.domain.vo.LockInfoPageParamVO;
import com.ruoyi.web.domain.vo.LockInfoViewVO;
import com.ruoyi.web.domain.vo.installationlist.LockInstallationPageParamVO;
import com.ruoyi.web.domain.vo.installationlist.LockInstallationViewVO;
import java.util.List;

public interface LockInfoMapper extends BaseMapper<LockInfo> {

    List<LockInfoViewVO> selectAllList(LockInfoPageParamVO vo);

    List<LockInstallationViewVO> selectInstallationList(LockInstallationPageParamVO vo);
}
