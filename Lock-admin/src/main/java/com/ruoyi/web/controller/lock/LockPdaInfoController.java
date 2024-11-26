package com.ruoyi.web.controller.lock;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.alibaba.fastjson2.JSON;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Maps;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.web.constants.LockCache;
import com.ruoyi.web.domain.LockPdaInfo;
import com.ruoyi.web.domain.vo.pda.LockPadPageParamVO;
import com.ruoyi.web.domain.vo.port.LockUnlockHexMessageVO;
import com.ruoyi.web.service.ILockPdaInfoService;
import com.ruoyi.web.service.ILockPdaUserService;
import com.ruoyi.web.service.ILockPortInfoService;
import com.ruoyi.web.service.ILockUnlockLogService;
import com.ruoyi.web.service.PdaService;
import com.ruoyi.web.utils.CommonUtils;
import com.ruoyi.web.utils.LockUtil;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lock/pdaInfo")
public class LockPdaInfoController extends BaseController {

    private final ILockPdaInfoService pdaInfoService;
    private final ILockUnlockLogService unlockLogService;
    private final ILockPdaUserService pdaUserService;

    private final ILockPortInfoService portInfoService;
    public static final String AES_KEY = "uuNbz89psCnbtJlm";

    @PostMapping("/list")
    public TableDataInfo list(@RequestBody LockPadPageParamVO padPageParamVO) {
        PageHelper.startPage(padPageParamVO.getPageNum(), padPageParamVO.getPageSize());
        List<LockPdaInfo> list = pdaInfoService.getPdaInfoList(padPageParamVO);
        return getDataTable(list);
    }

    @PostMapping("/saveOrUpdate")
    public AjaxResult saveOrUpdate(@RequestBody @Validated LockPdaInfo lockPdaInfo) {
        pdaInfoService.judgeKey(lockPdaInfo);
        CommonUtils.addCommonParams(lockPdaInfo, lockPdaInfo.getId());
        return toAjax(pdaInfoService.saveOrUpdateAll(lockPdaInfo));
    }

    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(pdaInfoService.deleteByIds(ids));
    }

    @SneakyThrows
    @PostMapping(value = "/downloadSynchronizationFile/{id}", produces = "application/octet-stream")
    public void downloadSynchronizationFile(@PathVariable String id, HttpServletResponse response) {
        LockPdaInfo pdaInfo = pdaInfoService.getById(id);
        Map<String, Object> map = setSynchronizationMap(pdaInfo);
        AES aes = SecureUtil.aes(AES_KEY.getBytes());
        String encryptHex = aes.encryptHex(JSON.toJSONString(map));
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        String fileName = URLEncoder.encode("同步文件", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition",
            "attachment;filename*=utf-8''" + fileName + ".cer");
        // 设置响应头信息
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        out.write(encryptHex);
        out.flush();
        out.close();
    }

    public static Map<String, Object> setSynchronizationMap(LockPdaInfo pdaInfo) {
        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("pdaMergeDataVO",
            SpringUtils.getBean(PdaService.class).getAllDataByPda(pdaInfo));
        dataMap.put("deviceId", pdaInfo.getOnlyKey());
        dataMap.put("licenseStrList", LockCache.licenseStrList);
        dataMap.put("licenseMaxNumber", LockCache.lockNumber);
        return dataMap;
    }

    @GetMapping("/getHexMessageForProofreadingTime")
    public AjaxResult getHexMessageForProofreadingTime() {
        return AjaxResult.success(
            LockUtil.bytesToHexWithSpaces(LockUtil.getByteForProofreadingTime()));
    }

    @GetMapping("/getHexMessageForUnlockLog/{start}")
    public AjaxResult getHexMessageForUnlockLog(@PathVariable Integer start) {
        return AjaxResult.success(
            LockUtil.bytesToHexWithSpaces(LockUtil.getByteForUnlockLog(start)));
    }

    @PostMapping("/syncKeyUnlockLog")
    public AjaxResult syncKeyUnlockLog(
        @RequestBody @Validated LockUnlockHexMessageVO lockUnlockHexMessageVO) {
        unlockLogService.syncKeyUnlockLog(lockUnlockHexMessageVO);
        return AjaxResult.success("正在后台同步数据,请耐心等待");
    }
}
