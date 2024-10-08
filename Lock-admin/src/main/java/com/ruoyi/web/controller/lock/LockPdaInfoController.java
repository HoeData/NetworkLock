package com.ruoyi.web.controller.lock;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.web.domain.LockPdaInfo;
import com.ruoyi.web.domain.vo.pda.LockPadPageParamVO;
import com.ruoyi.web.service.ILockPdaInfoService;
import com.ruoyi.web.utils.CommonUtils;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lock/pdaInfo")
public class LockPdaInfoController extends BaseController {

    @Resource
    private ILockPdaInfoService pdaInfoService;

    @PostMapping("/list")
    public TableDataInfo list(@RequestBody LockPadPageParamVO padPageParamVO) {
        PageHelper.startPage(padPageParamVO.getPageNum(), padPageParamVO.getPageSize());
        List<LockPdaInfo> list = pdaInfoService.getPdaInfoList(padPageParamVO);
        return getDataTable(list);
    }

    @PostMapping("/saveOrUpdate")
    public AjaxResult saveOrUpdate(@RequestBody LockPdaInfo lockPdaInfo) {
        pdaInfoService.judgeKey(lockPdaInfo);
        CommonUtils.addCommonParams(lockPdaInfo, lockPdaInfo.getId());
        return toAjax(pdaInfoService.saveOrUpdateAll(lockPdaInfo));
    }

    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(pdaInfoService.deleteByIds(ids));
    }


}
