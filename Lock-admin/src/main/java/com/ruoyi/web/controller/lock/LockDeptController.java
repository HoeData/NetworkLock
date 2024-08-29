package com.ruoyi.web.controller.lock;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.web.domain.LockDept;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.domain.vo.LockCommonViewVO;
import com.ruoyi.web.service.ILockDeptService;
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
@RequestMapping("/lock/dept")
public class LockDeptController extends BaseController {

    @Resource
    private ILockDeptService lockDeptService;

    @PostMapping("/list")
    public TableDataInfo list(@RequestBody LockCommonParamVO lockCommonParamVO) {
        PageHelper.startPage(lockCommonParamVO.getPageNum(), lockCommonParamVO.getPageSize());
        List<LockCommonViewVO> list = lockDeptService.selectDeptList(lockCommonParamVO);
        return getDataTable(list);
    }

    @GetMapping("/getAll")
    public AjaxResult getAll() {
        List<LockCommonViewVO> list = lockDeptService.selectDeptList(new LockCommonParamVO());
        return success(list);
    }

    @PostMapping("/saveOrUpdate")
    public AjaxResult saveOrUpdate(@RequestBody LockDept lockDept) {
        lockDeptService.judgeName(lockDept);
        CommonUtils.addCommonParams(lockDept, lockDept.getId());
        return toAjax(lockDeptService.saveOrUpdate(lockDept));
    }

    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(lockDeptService.deleteByIds(ids));
    }


}
