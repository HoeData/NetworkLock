package com.manniu.screen.controller;

import com.github.pagehelper.PageHelper;
import com.manniu.screen.domain.LockScreenElectronicLock;
import com.manniu.screen.domain.LockScreenNetworkControl;
import com.manniu.screen.service.ILockScreenElectronicLockService;
import com.manniu.screen.utils.CommonUtils;
import com.manniu.screen.vo.param.LockScreenElectronicLockPageParamVO;
import com.manniu.screen.vo.param.LockScreenNetworkControlPageParamVO;
import com.manniu.screen.vo.view.LockScreenElectronicLockViewVO;
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
@RequestMapping("/lockScreenElectronicLock")
@RequiredArgsConstructor
public class LockScreenElectronicLockController extends BaseController {
    private final ILockScreenElectronicLockService lockScreenElectronicLockService;

    @PostMapping("/saveOrUpdate")
    public AjaxResult saveOrUpdate(@RequestBody LockScreenElectronicLock electronicLock) {
        CommonUtils.addCommonParams(electronicLock, electronicLock.getId());
        return toAjax(lockScreenElectronicLockService.saveOrUpdate(electronicLock));
    }
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody LockScreenElectronicLockPageParamVO pageParamVO) {
        PageHelper.startPage(pageParamVO.getPageNum(), pageParamVO.getPageSize());
        List<LockScreenElectronicLockViewVO> list = lockScreenElectronicLockService.getElectronicLockList(pageParamVO);
        return getDataTable(list);
    }
}
