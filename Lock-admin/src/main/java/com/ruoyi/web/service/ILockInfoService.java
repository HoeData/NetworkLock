package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.web.domain.LockInfo;
import com.ruoyi.web.domain.vo.LockInfoPageParamVO;
import com.ruoyi.web.domain.vo.LockInfoViewVO;
import java.util.List;

public interface ILockInfoService extends IService<LockInfo> {

    List<LockInfoViewVO> selectAllList(LockInfoPageParamVO vo);
}
