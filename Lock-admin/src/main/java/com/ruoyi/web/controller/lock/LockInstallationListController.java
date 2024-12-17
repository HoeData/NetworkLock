package com.ruoyi.web.controller.lock;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;
import com.deepoove.poi.util.PoitlIOUtils;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Maps;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.web.core.config.TemplateProperties;
import com.ruoyi.web.domain.vo.installationlist.LockInstallationPageParamVO;
import com.ruoyi.web.domain.vo.installationlist.LockInstallationViewVO;
import com.ruoyi.web.service.ILockInfoService;
import com.ruoyi.web.service.ILockSiteService;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/installationList")
@RequiredArgsConstructor
public class LockInstallationListController extends BaseController {

    private final ILockInfoService lockInfoService;
    private final ILockSiteService lockSiteService;
    private final TemplateProperties templateProperties;

    @PostMapping("/list")
    public TableDataInfo list(@RequestBody LockInstallationPageParamVO vo) {
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<LockInstallationViewVO> list = lockInfoService.getInstallationList(vo);
        return getDataTable(list);
    }

    @PostMapping("/downloadInstallationList")
    public void downloadInstallationList(HttpServletResponse response,
        @RequestBody LockInstallationPageParamVO vo) {
        PageHelper.startPage(1, Integer.MAX_VALUE);
        Map<String, Object> dataMap = Maps.newHashMap();
        List<LockInstallationViewVO> list = lockInfoService.getInstallationList(vo);
        String siteName = lockSiteService.getById(vo.getSiteId()).getName();
        dataMap.put("installationList", list);
        dataMap.put("siteName", siteName);
        LoopRowTableRenderPolicy hackLoopTableRenderPolicy = new LoopRowTableRenderPolicy();
        Configure config = Configure.builder().bind(hackLoopTableRenderPolicy, "installationList")
            .build();
        XWPFTemplate template = XWPFTemplate.compile(templateProperties.getInstallationListPath(),
            config).render(dataMap);
        OutputStream out = null;
        BufferedOutputStream bos = null;
        try {
            String fileName = URLEncoder.encode(siteName + "智能端口锁安装清单", "UTF-8")
                .replaceAll("\\+", "%20");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition",
                "attachment;filename*=utf-8''" + fileName + ".docx");
            out = response.getOutputStream();
            bos = new BufferedOutputStream(out);
            template.write(bos);
            bos.flush();
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            PoitlIOUtils.closeQuietlyMulti(template, bos, out);
        }
    }
}
