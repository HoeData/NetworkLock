package com.ruoyi.web.controller.lock;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.service.LockIndexService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lock/index")
@RequiredArgsConstructor
public class LockIndexController extends BaseController {

    private final LockIndexService lockIndexService;
    @GetMapping("/getIndexStatisticalQuantity")
    public AjaxResult getIndexStatisticalQuantity(LockCommonParamVO vo) {
        return success(lockIndexService.getIndexStatisticalQuantity(vo));
    }
    @GetMapping("/getLockNumberByStatusAndSite")
    public AjaxResult getLockNumberByStatusAndSite() {
        return success(lockIndexService.getLockNumberByStatusAndSite());
    }
    @GetMapping("/getLockStatusListByLatitudeType")
    public AjaxResult getLockStatusListByLatitudeType(@RequestParam String type,@RequestParam Integer value) {
        return success(lockIndexService.getLockStatusListByLatitudeType(type,value));
    }
}
