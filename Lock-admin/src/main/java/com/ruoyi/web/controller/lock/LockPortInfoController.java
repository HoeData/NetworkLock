package com.ruoyi.web.controller.lock;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.web.domain.LockPortInfo;
import com.ruoyi.web.domain.vo.port.LockPortInfoListParamVO;
import com.ruoyi.web.service.ILockPortInfoService;
import com.ruoyi.web.utils.CommonUtils;
import com.ruoyi.web.utils.LockUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
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
    private static final String HEX_MESSAGE = "hexMessage";

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
        LockUtil.checkASCIILength(lockPortInfo.getUserCode(), "锁的用户码不正确,请重新输入");
        LockUtil.checkASCIILength(lockPortInfo.getKeyId(), "钥匙ID不正确,请重新输入");
        CommonUtils.addCommonParams(lockPortInfo, lockPortInfo.getId());
        return toAjax(lockPortInfoService.updateById(lockPortInfo));
    }

    @GetMapping("/getStatisticalQuantity")
    public AjaxResult getStatisticalQuantity() {
        return success(lockPortInfoService.getStatisticalQuantity());
    }

    @PostMapping("/getHexMessageForAddLock")
    public AjaxResult getHexMessageForAddLock(@RequestBody @Validated List<LockPortInfo> list) {
        Map<String, String> map = new HashMap<>(1);
        map.put(HEX_MESSAGE, lockPortInfoService.getHexMessageForAddLock(list));
        return success(map);
    }

    @PostMapping("/getHexMessageForDelLock")
    public AjaxResult getHexMessageForDelLock(@RequestBody  List<LockPortInfo> list) {
        Map<String, String> map = new HashMap<>(1);
        map.put(HEX_MESSAGE, lockPortInfoService.getHexMessageForDelLock(list));
        return success(map);
    }

}
