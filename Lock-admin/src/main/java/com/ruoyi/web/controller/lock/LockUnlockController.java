package com.ruoyi.web.controller.lock;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.web.domain.vo.pda.LockUnlockViewVO;
import com.ruoyi.web.domain.vo.pda.UnlockPageParamVO;
import com.ruoyi.web.service.ILockUnlockLogService;
import com.ruoyi.web.utils.EasyExcelUtil;
import java.time.LocalDateTime;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
    public TableDataInfo list(@RequestBody UnlockPageParamVO pageVO) {
        PageHelper.startPage(pageVO.getPageNum(), pageVO.getPageSize());
        List<LockUnlockViewVO> list = unlockLogService.selectUnlockList(pageVO);
        return getDataTable(list);
    }

    @PostMapping("/download")
    public void downloadFailedUsingJson(HttpServletResponse response,
        @RequestBody UnlockPageParamVO pageVO) {
        EasyExcelUtil.simpleDownload(LockUnlockViewVO.class, "开锁日志" + LocalDateTime.now(),
            "开锁日志", response, unlockLogService.selectUnlockList(pageVO));
    }
}
