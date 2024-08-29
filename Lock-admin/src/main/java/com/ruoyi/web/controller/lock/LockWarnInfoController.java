package com.ruoyi.web.controller.lock;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.web.domain.LockWarnInfo;
import com.ruoyi.web.domain.vo.LockEquipmentParamVO;
import com.ruoyi.web.domain.vo.LockWarnInfoViewVO;
import com.ruoyi.web.service.ILockWarnInfoService;
import com.ruoyi.web.utils.CommonUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lockWarnInfo")
public class LockWarnInfoController extends BaseController {

    private final ILockWarnInfoService warnInfoService;

    @GetMapping("/getTheLastWarn")
    public AjaxResult getTheLastWarn() {
        return success(warnInfoService.getTheLastWarn());
    }

    @PostMapping("/list")
    public TableDataInfo list(@RequestBody LockEquipmentParamVO lockEquipmentParamVO) {
        PageHelper.startPage(lockEquipmentParamVO.getPageNum(), lockEquipmentParamVO.getPageSize());
        List<LockWarnInfoViewVO> list = warnInfoService.selectWarnInfoList(lockEquipmentParamVO);
        return getDataTable(list);
    }

    @PostMapping("/confirm/{id}")
    public AjaxResult confirm(@PathVariable String id) {
        LockWarnInfo lockWarnInfo = warnInfoService.getById(id);
        lockWarnInfo.setStatus(1);
        CommonUtils.addCommonParams(lockWarnInfo,lockWarnInfo.getId());
        warnInfoService.updateById(lockWarnInfo);
        return success();
    }


}
