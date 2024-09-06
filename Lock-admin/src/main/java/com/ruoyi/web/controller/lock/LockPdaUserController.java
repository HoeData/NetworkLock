package com.ruoyi.web.controller.lock;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.web.constants.PdaUserConst;
import com.ruoyi.web.domain.LockPdaUser;
import com.ruoyi.web.domain.vo.pda.LockPdaUserPageParamVO;
import com.ruoyi.web.service.ILockPdaUserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lock/pdaUser")
@RequiredArgsConstructor
public class LockPdaUserController extends BaseController {

    private final ILockPdaUserService pdaUserService;

    @PostMapping("/list")
    public TableDataInfo list(@Validated @RequestBody LockPdaUserPageParamVO vo) {
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<LockPdaUser> list = pdaUserService.getPdaUserList(vo);
        return getDataTable(list);
    }

    @PostMapping("/saveOrUpdate")
    public AjaxResult saveOrUpdate(@RequestBody LockPdaUser pdaUser) {
        pdaUserService.judgeWhetherEdit(pdaUser.getPdaId());
        pdaUserService.judgeAdminFlag(pdaUser);
        pdaUserService.judgeUserName(pdaUser);
        pdaUser.setPassword(SecurityUtils.encryptPassword(PdaUserConst.DEFAULT_USER_PASSWORD));
        return toAjax(pdaUserService.saveOrUpdate(pdaUser));
    }

    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(pdaUserService.deleteByIds(ids));
    }
}
