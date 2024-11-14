package com.ruoyi.web.controller.lock;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.web.domain.LockCompany;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.service.ILockCompanyService;
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
@RequestMapping("/lock/company")
public class LockCompanyController extends BaseController {

    @Resource
    private ILockCompanyService lockCompanyService;

    @PostMapping("/list")
    public TableDataInfo list(@RequestBody LockCommonParamVO lockCommonParamVO) {
        PageHelper.startPage(1, Integer.MAX_VALUE);
        List<LockCompany> list = lockCompanyService.selectCompanyList(lockCommonParamVO);
        return getDataTable(list);
    }

    @GetMapping("/getAll")
    public AjaxResult getAll(LockCommonParamVO lockCommonParamVO) {
        PageHelper.startPage(1, Integer.MAX_VALUE);
        List<LockCompany> list = lockCompanyService.selectCompanyList(lockCommonParamVO);
        return success(lockCompanyService.buildTreeSelect(list));
    }

    @PostMapping("/saveOrUpdate")
    public AjaxResult saveOrUpdate(@RequestBody LockCompany lockCompany) {
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        if (!sysUser.isAdmin()) {
            if (null == lockCompany.getId()) {
                if (lockCompany.getParentId() == 0) {
                    lockCompany.setParentId(sysUser.getCompanyId());
                }
            } else {
                if (!lockCompanyService.getByIdCache(lockCompany.getId()).getParentId().equals(0)) {
                    lockCompany.setParentId(sysUser.getCompanyId());
                }
            }
        }
        lockCompanyService.judgeName(lockCompany.getName(), lockCompany.getId());
        CommonUtils.addCommonParams(lockCompany, lockCompany.getId());
        return toAjax(lockCompanyService.saveOrUpdateAll(lockCompany));
    }

    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(lockCompanyService.deleteByIds(ids));
    }
}
