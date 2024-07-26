package com.ruoyi.web.controller.lock;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.web.domain.LockPortInfo;
import com.ruoyi.web.domain.vo.LockPortInfoListParamVO;
import com.ruoyi.web.service.ILockPortInfoService;
import com.ruoyi.web.utils.CommonUtils;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lock/portInfo")
public class LockPortInfoController extends BaseController {
    @Resource
    private ILockPortInfoService lockPortInfoService;

    @PostMapping("/list")
    public AjaxResult list(@RequestBody LockPortInfoListParamVO portInfoListParamVO) {
        List<LockPortInfo> list = lockPortInfoService.selectPortInfoList(portInfoListParamVO);
        return success(list);
    }

    @GetMapping("/{id}")
    public AjaxResult getDetail(@PathVariable String id) {
        return success(lockPortInfoService.getById(id));
    }

    @PostMapping("/setInfo")
    public AjaxResult saveOrUpdate(@RequestBody LockPortInfo lockPortInfo) {
        CommonUtils.addCommonParams(lockPortInfo, lockPortInfo.getId());
        return toAjax(lockPortInfoService.updateById(lockPortInfo));
    }

    @GetMapping("/getStatisticalQuantity")
    public AjaxResult getStatisticalQuantity() {
        return success(lockPortInfoService.getStatisticalQuantity());
    }
}
