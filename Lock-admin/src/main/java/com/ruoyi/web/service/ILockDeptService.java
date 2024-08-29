package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.web.domain.LockDept;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.domain.vo.LockCommonViewVO;
import java.util.List;

public interface ILockDeptService extends IService<LockDept> {

    List<LockCommonViewVO> selectDeptList(LockCommonParamVO lockCommonParamVO);

    int deleteByIds(String[] ids);

    void judgeName(LockDept lockDept);
}
