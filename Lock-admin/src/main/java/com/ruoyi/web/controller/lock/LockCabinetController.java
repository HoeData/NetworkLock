package com.ruoyi.web.controller.lock;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.web.domain.LockCabinet;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.domain.vo.LockCommonViewVO;
import com.ruoyi.web.service.ILockCabinetService;
import com.ruoyi.web.utils.CommonUtils;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lock/cabinet")
public class LockCabinetController extends BaseController {

    @Resource
    private ILockCabinetService lockCabinetService;

    @PostMapping("/list")
    public TableDataInfo list(@RequestBody LockCommonParamVO lockCommonParamVO) {
        PageHelper.startPage(lockCommonParamVO.getPageNum(), lockCommonParamVO.getPageSize());
        List<LockCommonViewVO> list = lockCabinetService.selectCabinetList(lockCommonParamVO);
        return getDataTable(list);
    }

    @GetMapping("/getAll")
    public AjaxResult getAll(LockCommonParamVO lockCommonParamVO) {
        PageHelper.startPage(1, Integer.MAX_VALUE);
        List<LockCommonViewVO> list = lockCabinetService.selectCabinetList(lockCommonParamVO);
        return success(list);
    }

    @PostMapping("/saveOrUpdate")
    public AjaxResult saveOrUpdate(@RequestBody LockCabinet lockCabinet) {
        lockCabinetService.judgeName(lockCabinet);
        CommonUtils.addCommonParams(lockCabinet, lockCabinet.getId());
        return toAjax(lockCabinetService.saveOrUpdate(lockCabinet));
    }

    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(lockCabinetService.deleteByIds(ids));
    }


}
