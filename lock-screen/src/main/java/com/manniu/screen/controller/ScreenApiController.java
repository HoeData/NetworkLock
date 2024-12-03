package com.manniu.screen.controller;

import com.ruoyi.common.core.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/aa")
public class ScreenApiController extends BaseController {

    @RequestMapping("/bb")
    public String bb() {
        return "bb";
    }
}
