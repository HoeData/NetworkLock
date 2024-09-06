package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.web.domain.vo.pda.LockPadPageParamVO;
import com.ruoyi.web.domain.LockPdaInfo;
import java.util.List;

public interface ILockPdaInfoService extends IService<LockPdaInfo> {

    void judgeKey(LockPdaInfo lockPdaInfo);

    List<LockPdaInfo> getPdaInfoList(LockPadPageParamVO padPageParamVO);

    int deleteByIds(String[] ids);

    LockPdaInfo getByKey(String key);

    int saveOrUpdateAll(LockPdaInfo lockPdaInfo);
}
