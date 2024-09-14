package com.ruoyi.web.controller.lock;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.web.domain.LockPdaDataSynchronizationInfo;
import com.ruoyi.web.domain.vo.pda.LockPdaDataSynchronizationInfoPageParamVO;
import com.ruoyi.web.domain.vo.pda.LockPdaDataSynchronizationInfoViewVO;
import com.ruoyi.web.service.ILockPdaDataSynchronizationInfoService;
import java.util.List;
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
    public TableDataInfo list(@RequestBody LockPdaDataSynchronizationInfoPageParamVO viewVO) {
        PageHelper.startPage(viewVO.getPageNum(), viewVO.getPageSize());
        List<LockPdaDataSynchronizationInfoViewVO> list = pdaDataSynchronizationInfoService.selectSynchronizationInfoList(viewVO);
        return getDataTable(list);
    }
}
