package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.domain.LockCabinet;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.domain.vo.LockCommonViewVO;
import com.ruoyi.web.mapper.LockCabinetMapper;
import com.ruoyi.web.service.ILockCabinetService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class LockCabinetServiceImpl extends
    ServiceImpl<LockCabinetMapper, LockCabinet> implements ILockCabinetService {

    @Resource
    private LockCabinetMapper lockCabinetMapper;

    @Override
    public List<LockCommonViewVO> selectCabinetList(LockCommonParamVO lockCommonParamVO) {
        return lockCabinetMapper.selectCabinetList(lockCommonParamVO);
    }

    @Override
    public int deleteByIds(String[] ids) {
        return lockCabinetMapper.deleteByIds(ids);
    }
}
