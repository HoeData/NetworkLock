package com.ruoyi.web.controller.lock;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.web.domain.vo.pda.UnlockPageParamVO;
import com.ruoyi.web.domain.vo.pda.UnlockViewVO;
import com.ruoyi.web.service.ILockAuthorizationLogService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lock/authorizationLog")
@RequiredArgsConstructor
public class LockAuthorizationLogController extends BaseController {

    private final ILockAuthorizationLogService lockAuthorizationLogService;

    @PostMapping("/list")
    public TableDataInfo allList(@RequestBody UnlockPageParamVO vo) {
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<UnlockViewVO> list = lockAuthorizationLogService.getAllList(vo);
        return getDataTable(list);
    }


}
