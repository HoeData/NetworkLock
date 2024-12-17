package com.ruoyi.web.controller.lock;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.web.domain.vo.LockInfoPageParamVO;
import com.ruoyi.web.domain.vo.LockInfoViewVO;
import com.ruoyi.web.service.ILockInfoService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lock/info")
@RequiredArgsConstructor
public class LockInfoController extends BaseController {

    private final ILockInfoService lockInfoService;
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody LockInfoPageParamVO vo) {
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<LockInfoViewVO> list = lockInfoService.getAllList(vo);
        return getDataTable(list);
    }
}
