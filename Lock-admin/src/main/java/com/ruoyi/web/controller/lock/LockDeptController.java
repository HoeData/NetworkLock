package com.ruoyi.web.controller.lock;

import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Maps;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.web.domain.LockDept;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.domain.vo.LockCommonViewVO;
import com.ruoyi.web.domain.vo.importvo.ImportDeptVO;
import com.ruoyi.web.listener.ImportDeptListener;
import com.ruoyi.web.listener.ImportErrorCache;
import com.ruoyi.web.service.ILockDeptService;
import com.ruoyi.web.utils.CommonUtils;
import com.ruoyi.web.utils.EasyExcelUtil;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/lock/dept")
public class LockDeptController extends BaseController {

    @Resource
    private ILockDeptService lockDeptService;

    @PostMapping("/list")
    public TableDataInfo list(@RequestBody LockCommonParamVO lockCommonParamVO) {
        PageHelper.startPage(lockCommonParamVO.getPageNum(), lockCommonParamVO.getPageSize());
        List<LockCommonViewVO> list = lockDeptService.selectDeptList(lockCommonParamVO);
        return getDataTable(list);
    }

    @GetMapping("/getAll")
    public AjaxResult getAll(LockCommonParamVO lockCommonParamVO) {
        PageHelper.startPage(1, Integer.MAX_VALUE);
        List<LockCommonViewVO> list = lockDeptService.selectDeptList(lockCommonParamVO);
        return success(list);
    }

    @PostMapping("/saveOrUpdate")
    public AjaxResult saveOrUpdate(@RequestBody LockDept lockDept) {
        lockDeptService.judgeName(lockDept);
        CommonUtils.addCommonParams(lockDept, lockDept.getId());
        return toAjax(lockDeptService.saveOrUpdate(lockDept));
    }

    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(lockDeptService.deleteByIds(ids));
    }

    @PostMapping("/import")
    @ResponseBody
    public AjaxResult upload(@RequestParam("file") MultipartFile file) throws IOException {
        ImportDeptListener importDeptListener = new ImportDeptListener();
        EasyExcel.read(file.getInputStream(), ImportDeptVO.class, importDeptListener).sheet()
            .doRead();
        if (StringUtils.isNotBlank(importDeptListener.getUuid())) {
            Map<String, String> map = Maps.newHashMap();
            map.put("downloadId", importDeptListener.getUuid());
            return AjaxResult.success("数据校验失败，已为您下载错误原因EXCEL,请修改后重新上传", map);
        }
        return AjaxResult.success("部门导入成功");
    }

    @PostMapping("/download/{downloadId}")
    public void downloadFailedUsingJson(HttpServletResponse response,
        @PathVariable String downloadId) {
        EasyExcelUtil.simpleDownload(ImportDeptVO.class, "部门导入错误信息" + LocalDateTime.now(),
            "部门导入错误信息", response,
            (List<ImportDeptVO>) ImportErrorCache.errorMap.get(downloadId));
        ImportErrorCache.errorMap.remove(downloadId);
    }

}
