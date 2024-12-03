package com.manniu.screen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manniu.screen.mapper.TestMapper;
import com.manniu.screen.service.ITestService;
import com.manniu.screen.domain.Test;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, Test> implements
    ITestService {

}
