package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.web.domain.LockInfo;
import com.ruoyi.web.domain.vo.LockInfoPageParamVO;
import com.ruoyi.web.domain.vo.LockInfoViewVO;
import com.ruoyi.web.domain.vo.installationlist.LockInstallationPageParamVO;
import com.ruoyi.web.domain.vo.installationlist.LockInstallationViewVO;
import java.util.List;

public interface ILockInfoService extends IService<LockInfo> {

    List<LockInfoViewVO> getAllList(LockInfoPageParamVO vo);

    List<LockInstallationViewVO> getInstallationList(LockInstallationPageParamVO vo);
}
