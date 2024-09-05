package com.ruoyi.web.controller.lock;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.web.domain.RelPdaUserPort;
import com.ruoyi.web.domain.vo.RelPdaUserPortParamVO;
import com.ruoyi.web.domain.vo.RelPdaUserPortViewVO;
import com.ruoyi.web.service.IRelPdaUserPortService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rel/pdaUserPort")
@RequiredArgsConstructor
public class RelPdaUserPortController extends BaseController {

    private final IRelPdaUserPortService relPdaUserPortService;

    @PostMapping("/allList")
    public TableDataInfo allList(@RequestBody RelPdaUserPortParamVO vo) {
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<RelPdaUserPortViewVO> list = relPdaUserPortService.getAllList(vo);
        return getDataTable(list);
    }

    @PostMapping("/authorizationList")
    public TableDataInfo authorizationList(@Validated @RequestBody RelPdaUserPortParamVO vo) {
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<RelPdaUserPortViewVO> list = relPdaUserPortService.getAuthorizationList(vo);
        return getDataTable(list);
    }

    @PostMapping("/saveAuthorization")
    public AjaxResult saveAuthorization(@RequestBody List<RelPdaUserPort> list) {
        return toAjax(relPdaUserPortService.saveAuthorization(list));
    }
}
