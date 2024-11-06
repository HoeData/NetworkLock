package com.manniu.offline.controller;

import com.google.common.collect.Maps;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lock/pdaSynchronization")
public class PdaSynchronizationController {

    @GetMapping("/start")
    public Map<String, Object> error() {
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("code", 200);
        resultMap.put("msg", "同步成功");
        //TODO 开始同步代码
        return resultMap;
    }
}
