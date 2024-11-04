package com.ruoyi.web.controller.lock;

import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Maps;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.web.domain.LockEquipmentType;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.domain.vo.importvo.ImportEquipmentModelVO;
import com.ruoyi.web.domain.vo.importvo.ImportEquipmentTypeVO;
import com.ruoyi.web.listener.ImportEquipmentModelListener;
import com.ruoyi.web.listener.ImportEquipmentTypeListener;
import com.ruoyi.web.listener.ImportErrorCache;
import com.ruoyi.web.service.ILockEquipmentTypeService;
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
@RequestMapping("/lock/equipmentType")
public class LockEquipmentTypeController extends BaseController {

    @Resource
    private ILockEquipmentTypeService lockEquipmentTypeService;

    @PostMapping("/list")
    public TableDataInfo list(@RequestBody LockCommonParamVO lockCommonParamVO) {
        PageHelper.startPage(lockCommonParamVO.getPageNum(), lockCommonParamVO.getPageSize());
        List<LockEquipmentType> list = lockEquipmentTypeService.selectEquipmentTypeList(lockCommonParamVO);
        return getDataTable(list);
    }

    @GetMapping("/getAll")
    public AjaxResult getAll() {
        PageHelper.startPage(1, Integer.MAX_VALUE);
        List<LockEquipmentType> list = lockEquipmentTypeService.selectEquipmentTypeList(new LockCommonParamVO());
        return success(list);
    }

    @PostMapping("/saveOrUpdate")
    public AjaxResult saveOrUpdate(@RequestBody LockEquipmentType lockEquipmentType) {
        lockEquipmentTypeService.judgeName(lockEquipmentType);
        CommonUtils.addCommonParams(lockEquipmentType, lockEquipmentType.getId());
        return toAjax(lockEquipmentTypeService.saveOrUpdate(lockEquipmentType));
    }

    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(lockEquipmentTypeService.deleteByIds(ids));
    }

    @PostMapping("/import")
    @ResponseBody
    public AjaxResult upload(@RequestParam("file") MultipartFile file) throws IOException {
        ImportEquipmentTypeListener importEquipmentTypeListener = new ImportEquipmentTypeListener();
        EasyExcel.read(file.getInputStream(), ImportEquipmentTypeVO.class, importEquipmentTypeListener).sheet()
            .doRead();
        if (StringUtils.isNotBlank(importEquipmentTypeListener.getUuid())) {
            Map<String, String> map = Maps.newHashMap();
            map.put("downloadId", importEquipmentTypeListener.getUuid());
            return AjaxResult.error("数据校验失败，请下载错误原因EXCEL,修改后重新上传", map);
        }
        return AjaxResult.success("设备类型导入成功");
    }

    @GetMapping("/download/{downloadId}")
    public void downloadFailedUsingJson(HttpServletResponse response,
        @PathVariable String downloadId) {
        EasyExcelUtil.simpleDownload(ImportEquipmentTypeVO.class,
            "设备类型导入错误信息" + LocalDateTime.now(), "设备类型导入错误信息", response,
            (List<ImportEquipmentTypeVO>) ImportErrorCache.errorMap.get(downloadId));
        ImportErrorCache.errorMap.remove(downloadId);
    }
}
