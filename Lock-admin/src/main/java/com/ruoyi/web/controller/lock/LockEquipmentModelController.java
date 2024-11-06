package com.ruoyi.web.controller.lock;

import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Maps;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.web.domain.LockEquipmentModel;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.domain.vo.importvo.ImportCabinetVO;
import com.ruoyi.web.domain.vo.importvo.ImportEquipmentModelVO;
import com.ruoyi.web.listener.ImportCabinetListener;
import com.ruoyi.web.listener.ImportEquipmentModelListener;
import com.ruoyi.web.listener.ImportErrorCache;
import com.ruoyi.web.service.ILockEquipmentModelService;
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
@RequestMapping("/lock/equipmentModel")
public class LockEquipmentModelController extends BaseController {

    @Resource
    private ILockEquipmentModelService lockEquipmentModelService;

    @PostMapping("/list")
    public TableDataInfo list(@RequestBody LockCommonParamVO lockCommonParamVO) {
        PageHelper.startPage(lockCommonParamVO.getPageNum(), lockCommonParamVO.getPageSize());
        List<LockEquipmentModel> list = lockEquipmentModelService.selectEquipmentModelList(lockCommonParamVO);
        return getDataTable(list);
    }

    @GetMapping("/getAll")
    public AjaxResult getAll() {
        PageHelper.startPage(1, Integer.MAX_VALUE);
        List<LockEquipmentModel> list = lockEquipmentModelService.selectEquipmentModelList(new LockCommonParamVO());
        return success(list);
    }

    @PostMapping("/saveOrUpdate")
    public AjaxResult saveOrUpdate(@RequestBody LockEquipmentModel lockEquipmentModel) {
        lockEquipmentModelService.judgeName(lockEquipmentModel);
        CommonUtils.addCommonParams(lockEquipmentModel, lockEquipmentModel.getId());
        return toAjax(lockEquipmentModelService.saveOrUpdate(lockEquipmentModel));
    }

    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(lockEquipmentModelService.deleteByIds(ids));
    }
    @PostMapping("/import")
    @ResponseBody
    public AjaxResult upload(@RequestParam("file") MultipartFile file) throws IOException {
        ImportEquipmentModelListener importEquipmentModelListener = new ImportEquipmentModelListener();
        EasyExcel.read(file.getInputStream(), ImportEquipmentModelVO.class, importEquipmentModelListener).sheet()
            .doRead();
        if (StringUtils.isNotBlank(importEquipmentModelListener.getUuid())) {
            Map<String, String> map = Maps.newHashMap();
            map.put("downloadId", importEquipmentModelListener.getUuid());
            return AjaxResult.success("数据校验失败，已为您下载错误原因EXCEL,请修改后重新上传", map);
        }
        return AjaxResult.success("设备型号导入成功");
    }

    @PostMapping("/download/{downloadId}")
    public void downloadFailedUsingJson(HttpServletResponse response,
        @PathVariable String downloadId) {
        EasyExcelUtil.simpleDownload(ImportEquipmentModelVO.class,
            "设备型号导入错误信息" + LocalDateTime.now(), "设备型号导入错误信息", response,
            (List<ImportEquipmentModelVO>) ImportErrorCache.errorMap.get(downloadId));
        ImportErrorCache.errorMap.remove(downloadId);
    }

}
