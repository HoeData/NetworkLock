package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.domain.LockInfo;
import com.ruoyi.web.mapper.LockInfoMapper;
import com.ruoyi.web.service.ILockInfoService;
import org.springframework.stereotype.Service;

@Service
public class LockInfoServiceImpl extends ServiceImpl<LockInfoMapper, LockInfo> implements
    ILockInfoService {

}
