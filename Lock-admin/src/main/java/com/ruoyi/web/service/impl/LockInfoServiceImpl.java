package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.domain.LockInfo;
import com.ruoyi.web.domain.vo.LockInfoPageParamVO;
import com.ruoyi.web.domain.vo.LockInfoViewVO;
import com.ruoyi.web.domain.vo.installationlist.LockInstallationPageParamVO;
import com.ruoyi.web.domain.vo.installationlist.LockInstallationViewVO;
import com.ruoyi.web.mapper.LockInfoMapper;
import com.ruoyi.web.service.ILockInfoService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LockInfoServiceImpl extends ServiceImpl<LockInfoMapper, LockInfo> implements
    ILockInfoService {

    private final LockInfoMapper lockInfoMapper;

    @Override
    public List<LockInfoViewVO> getAllList(LockInfoPageParamVO vo) {
        return lockInfoMapper.selectAllList(vo);
    }

    @Override
    public List<LockInstallationViewVO> getInstallationList(LockInstallationPageParamVO vo) {
        return lockInfoMapper.selectInstallationList(vo);
    }
}
