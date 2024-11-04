package com.ruoyi.web.controller.lock;

import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Maps;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.web.domain.LockCabinet;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.domain.vo.LockCommonViewVO;
import com.ruoyi.web.domain.vo.importvo.ImportCabinetVO;
import com.ruoyi.web.listener.ImportCabinetListener;
import com.ruoyi.web.listener.ImportErrorCache;
import com.ruoyi.web.service.ILockCabinetService;
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
@RequestMapping("/lock/cabinet")
public class LockCabinetController extends BaseController {

    @Resource
    private ILockCabinetService lockCabinetService;

    @PostMapping("/list")
    public TableDataInfo list(@RequestBody LockCommonParamVO lockCommonParamVO) {
        PageHelper.startPage(lockCommonParamVO.getPageNum(), lockCommonParamVO.getPageSize());
        List<LockCommonViewVO> list = lockCabinetService.selectCabinetList(lockCommonParamVO);
        return getDataTable(list);
    }

    @GetMapping("/getAll")
    public AjaxResult getAll(LockCommonParamVO lockCommonParamVO) {
        PageHelper.startPage(1, Integer.MAX_VALUE);
        List<LockCommonViewVO> list = lockCabinetService.selectCabinetList(lockCommonParamVO);
        return success(list);
    }

    @PostMapping("/saveOrUpdate")
    public AjaxResult saveOrUpdate(@RequestBody LockCabinet lockCabinet) {
        lockCabinetService.judgeName(lockCabinet);
        CommonUtils.addCommonParams(lockCabinet, lockCabinet.getId());
        return toAjax(lockCabinetService.saveOrUpdate(lockCabinet));
    }

    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(lockCabinetService.deleteByIds(ids));
    }

    @PostMapping("/import")
    @ResponseBody
    public AjaxResult upload(@RequestParam("file") MultipartFile file) throws IOException {
        ImportCabinetListener importCabinetListener = new ImportCabinetListener();
        EasyExcel.read(file.getInputStream(), ImportCabinetVO.class, importCabinetListener).sheet()
            .doRead();
        if (StringUtils.isNotBlank(importCabinetListener.getUuid())) {
            Map<String, String> map = Maps.newHashMap();
            map.put("downloadId", importCabinetListener.getUuid());
            return AjaxResult.error("数据校验失败，请下载错误原因EXCEL,修改后重新上传", map);
        }
        return AjaxResult.success("机柜导入成功");
    }

    @GetMapping("/download/{downloadId}")
    public void downloadFailedUsingJson(HttpServletResponse response,
        @PathVariable String downloadId) {
        EasyExcelUtil.simpleDownload(ImportCabinetVO.class,
            "机柜导入错误信息" + LocalDateTime.now(), "机柜导入错误信息", response,
            (List<ImportCabinetVO>) ImportErrorCache.errorMap.get(downloadId));
        ImportErrorCache.errorMap.remove(downloadId);
    }
}
