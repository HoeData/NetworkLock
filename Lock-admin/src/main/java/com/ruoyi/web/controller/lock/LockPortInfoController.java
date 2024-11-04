package com.ruoyi.web.controller.lock;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.web.domain.LockPortInfo;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.domain.vo.port.LockPortInfoListParamVO;
import com.ruoyi.web.service.ILockPortInfoService;
import com.ruoyi.web.utils.CommonUtils;
import com.ruoyi.web.utils.LockUtil;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
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
        PageHelper.startPage(1, Integer.MAX_VALUE);
        List<LockPortInfo> list = lockPortInfoService.selectPortInfoList(portInfoListParamVO);
        return success(list);
    }

    @GetMapping("/{id}")
    public AjaxResult getDetail(@PathVariable String id) {
        return success(lockPortInfoService.getById(id));
    }

    @PostMapping("/setInfo")
    public AjaxResult saveOrUpdate(@RequestBody LockPortInfo lockPortInfo) {
        if (StringUtils.isNotBlank(lockPortInfo.getUserCode())) {
            LockUtil.checkASCIILength(lockPortInfo.getUserCode(), "锁的用户码不正确,请重新输入");
            lockPortInfoService.judgeUserCode(lockPortInfo.getId(), lockPortInfo.getUserCode());
        }
        CommonUtils.addCommonParams(lockPortInfo, lockPortInfo.getId());
        return toAjax(lockPortInfoService.updateById(lockPortInfo));
    }
    @GetMapping("/getStatisticalQuantity")
    public AjaxResult getStatisticalQuantity(LockCommonParamVO vo) {
        return success(lockPortInfoService.getStatisticalQuantity(vo));
    }

}
