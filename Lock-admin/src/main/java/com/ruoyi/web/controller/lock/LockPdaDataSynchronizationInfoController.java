package com.ruoyi.web.controller.lock;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.web.domain.vo.pda.LockPdaDataSynchronizationInfoPageParamVO;
import com.ruoyi.web.domain.vo.pda.LockPdaDataSynchronizationInfoViewVO;
import com.ruoyi.web.domain.vo.pda.LockUnlockViewVO;
import com.ruoyi.web.domain.vo.pda.UnlockPageParamVO;
import com.ruoyi.web.service.ILockPdaDataSynchronizationInfoService;
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
@RequestMapping("/lock/pdaDataSynchronizationInfo")
@RequiredArgsConstructor
public class LockPdaDataSynchronizationInfoController extends BaseController {

    private final ILockPdaDataSynchronizationInfoService pdaDataSynchronizationInfoService;

    @PostMapping("/list")
    public TableDataInfo list(@RequestBody LockPdaDataSynchronizationInfoPageParamVO pageVO) {
        PageHelper.startPage(pageVO.getPageNum(), pageVO.getPageSize());
        List<LockPdaDataSynchronizationInfoViewVO> list = pdaDataSynchronizationInfoService.selectSynchronizationInfoList(pageVO);
        return getDataTable(list);
    }
    @PostMapping("/download")
    public void downloadFailedUsingJson(HttpServletResponse response,
        @RequestBody LockPdaDataSynchronizationInfoPageParamVO pageVO) {
        EasyExcelUtil.simpleDownload(LockPdaDataSynchronizationInfoViewVO.class, "同步日志" + LocalDateTime.now(),
            "同步日志", response, pdaDataSynchronizationInfoService.selectSynchronizationInfoList(pageVO));
    }
}
