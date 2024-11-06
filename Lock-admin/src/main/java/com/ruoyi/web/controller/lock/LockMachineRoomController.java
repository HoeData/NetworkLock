package com.ruoyi.web.controller.lock;

import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Maps;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.web.domain.LockMachineRoom;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.domain.vo.LockCommonViewVO;
import com.ruoyi.web.domain.vo.importvo.ImportMachineRoomVO;
import com.ruoyi.web.listener.ImportErrorCache;
import com.ruoyi.web.listener.ImportMachineRoomListener;
import com.ruoyi.web.service.ILockMachineRoomService;
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
@RequestMapping("/lock/machineRoom")
public class LockMachineRoomController extends BaseController {

    @Resource
    private ILockMachineRoomService lockMachineRoomService;

    @PostMapping("/list")
    public TableDataInfo list(@RequestBody LockCommonParamVO lockCommonParamVO) {
        PageHelper.startPage(lockCommonParamVO.getPageNum(), lockCommonParamVO.getPageSize());
        List<LockCommonViewVO> list = lockMachineRoomService.selectMachineRoomList(
            lockCommonParamVO);
        return getDataTable(list);
    }

    @GetMapping("/getAll")
    public AjaxResult getAll(LockCommonParamVO lockCommonParamVO) {
        PageHelper.startPage(1, Integer.MAX_VALUE);
        List<LockCommonViewVO> list = lockMachineRoomService.selectMachineRoomList(lockCommonParamVO);
        return success(list);
    }

    @PostMapping("/saveOrUpdate")
    public AjaxResult saveOrUpdate(@RequestBody LockMachineRoom lockMachineRoom) {
        lockMachineRoomService.judgeName(lockMachineRoom);
        CommonUtils.addCommonParams(lockMachineRoom, lockMachineRoom.getId());
        return toAjax(lockMachineRoomService.saveOrUpdate(lockMachineRoom));
    }

    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(lockMachineRoomService.deleteByIds(ids));
    }

    @PostMapping("/import")
    @ResponseBody
    public AjaxResult upload(@RequestParam("file") MultipartFile file) throws IOException {
        ImportMachineRoomListener importMachineRoomListener = new ImportMachineRoomListener();
        EasyExcel.read(file.getInputStream(), ImportMachineRoomVO.class, importMachineRoomListener)
            .sheet()
            .doRead();
        if (StringUtils.isNotBlank(importMachineRoomListener.getUuid())) {
            Map<String, String> map = Maps.newHashMap();
            map.put("downloadId", importMachineRoomListener.getUuid());
            return AjaxResult.success("数据校验失败，已为您下载错误原因EXCEL,请修改后重新上传", map);
        }
        return AjaxResult.success("机房导入成功");
    }

    @PostMapping("/download/{downloadId}")
    public void downloadFailedUsingJson(HttpServletResponse response,
        @PathVariable String downloadId) {
        EasyExcelUtil.simpleDownload(ImportMachineRoomVO.class,
            "机房导入错误信息" + LocalDateTime.now(),
            "机房导入错误信息", response,
            (List<ImportMachineRoomVO>) ImportErrorCache.errorMap.get(downloadId));
        ImportErrorCache.errorMap.remove(downloadId);
    }

}
