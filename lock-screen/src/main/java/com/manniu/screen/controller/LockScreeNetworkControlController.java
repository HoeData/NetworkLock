package com.manniu.screen.controller;

import com.github.pagehelper.PageHelper;
import com.manniu.screen.domain.LockScreenNetworkControl;
import com.manniu.screen.service.ILockScreenNetworkControlService;
import com.manniu.screen.utils.CommonUtils;
import com.manniu.screen.vo.param.LockScreenNetworkControlPageParamVO;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lockScreenNetworkControl")
@RequiredArgsConstructor
public class LockScreeNetworkControlController extends BaseController {

    private final ILockScreenNetworkControlService networkControlService;

    @PostMapping("/saveOrUpdate")
    public AjaxResult saveOrUpdate(@RequestBody LockScreenNetworkControl networkControl) {
        CommonUtils.addCommonParams(networkControl, networkControl.getId());
        return toAjax(networkControlService.saveOrUpdate(networkControl));
    }
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody LockScreenNetworkControlPageParamVO pageParamVO) {
        PageHelper.startPage(pageParamVO.getPageNum(), pageParamVO.getPageSize());
        List<LockScreenNetworkControl> list = networkControlService.getNetworkControlList(pageParamVO);
        return getDataTable(list);
    }
}
