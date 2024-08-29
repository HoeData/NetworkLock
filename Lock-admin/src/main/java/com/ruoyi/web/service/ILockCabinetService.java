package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.web.domain.LockCabinet;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.domain.vo.LockCommonViewVO;
import java.util.List;

public interface ILockCabinetService extends IService<LockCabinet> {


    List<LockCommonViewVO> selectCabinetList(LockCommonParamVO lockCommonParamVO);

    int deleteByIds(String[] ids);

    void judgeName(LockCabinet lockCabinet);
}
