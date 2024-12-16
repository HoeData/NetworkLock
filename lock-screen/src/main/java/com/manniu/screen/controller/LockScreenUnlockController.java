package com.manniu.screen.controller;

import com.github.pagehelper.PageHelper;
import com.manniu.screen.service.ILockScreenUnlockService;
import com.manniu.screen.vo.param.LockScreenUnlockPageParamVO;
import com.manniu.screen.vo.view.LockScreenUnlockVIewVO;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lockScreenUnlock")
@RequiredArgsConstructor
public class LockScreenUnlockController extends BaseController {

    private final ILockScreenUnlockService unlockService;

    @PostMapping("/list")
    public TableDataInfo list(@RequestBody LockScreenUnlockPageParamVO pageParamVO) {
        PageHelper.startPage(pageParamVO.getPageNum(), pageParamVO.getPageSize());
        List<LockScreenUnlockVIewVO> list = unlockService.getUnlockList(pageParamVO);
        return getDataTable(list);
    }
}
