package com.ruoyi.web.controller.lock;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.web.domain.LockMonitorFlow;
import com.ruoyi.web.domain.vo.LockMonitorFlowPageParamVO;
import com.ruoyi.web.service.ILockMonitorFlowService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lockMonitorFlow")
@RequiredArgsConstructor
public class LockMonitorFlowController extends BaseController {

    private final ILockMonitorFlowService monitorFlowService;

    @PostMapping("/list")
    public TableDataInfo list(@Validated @RequestBody LockMonitorFlowPageParamVO vo) {
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<LockMonitorFlow> list = monitorFlowService.selectMonitorFlowList(vo);
        return getDataTable(list);
    }
    @PostMapping("/getWeekTrend")
    public AjaxResult getWeekTrend(@Validated @RequestBody LockMonitorFlowPageParamVO vo){
      return AjaxResult.success(monitorFlowService.getWeekTrend(vo));
    }
}
