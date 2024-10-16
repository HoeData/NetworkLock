package com.ruoyi.web.controller.lock;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.web.domain.RelPdaUserPort;
import com.ruoyi.web.domain.vo.pda.RelPdaUserPortParamVO;
import com.ruoyi.web.domain.vo.pda.RelPdaUserPortViewVO;
import com.ruoyi.web.domain.vo.port.LockInfoForAddLockVO;
import com.ruoyi.web.service.IRelPdaUserPortService;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(relPdaUserPortService.removeByIds(Arrays.asList(ids)));
    }

    @PostMapping("/getHexMessageForAddLock")
    public AjaxResult getHexMessageForAddLock(
        @RequestBody List<RelPdaUserPort> relPdaUserPortList) {
        return success(relPdaUserPortService.getHexMessageForAddLock(relPdaUserPortList));
    }

    @GetMapping("/getHexMessageForDelLock/{ids}")
    public AjaxResult getHexMessageForDelLock(@PathVariable String[] ids) {
        Map<String, String> map = new HashMap<>(1);
        map.put("hexMessage", relPdaUserPortService.getHexMessageForDelLock(ids));
        return success(map);
    }
}
