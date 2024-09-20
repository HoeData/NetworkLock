package com.ruoyi.web.controller.lock;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.web.domain.vo.PageVO;
import com.ruoyi.web.domain.vo.pda.LockUnlockViewVO;
import com.ruoyi.web.service.ILockUnlockLogService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lock/unlock")
@RequiredArgsConstructor
public class LockUnlockController extends BaseController {

    private final ILockUnlockLogService unlockLogService;

    @PostMapping("/list")
    public TableDataInfo list(@RequestBody PageVO pageVO) {
        PageHelper.startPage(pageVO.getPageNum(), pageVO.getPageSize());
        List<LockUnlockViewVO> list = unlockLogService.selectUnlockList(pageVO);
        return getDataTable(list);
    }
}
