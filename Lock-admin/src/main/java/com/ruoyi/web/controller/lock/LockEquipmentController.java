package com.ruoyi.web.controller.lock;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.web.domain.vo.LockEquipmentAddParamVO;
import com.ruoyi.web.domain.vo.LockEquipmentParamVO;
import com.ruoyi.web.domain.vo.LockEquipmentViewVO;
import com.ruoyi.web.service.ILockEquipmentService;
import com.ruoyi.web.utils.CommonUtils;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lock/equipment")
public class LockEquipmentController extends BaseController {

    @Resource
    private ILockEquipmentService lockEquipmentService;

    @PostMapping("/list")
    public TableDataInfo list(@RequestBody LockEquipmentParamVO lockEquipmentParamVO) {
        PageHelper.startPage(lockEquipmentParamVO.getPageNum(), lockEquipmentParamVO.getPageSize());
        List<LockEquipmentViewVO> list = lockEquipmentService.selectEquipmentList(
            lockEquipmentParamVO);
        return getDataTable(list);
    }

    @GetMapping("/getAll")
    public AjaxResult getAll() {
        List<LockEquipmentViewVO> list = lockEquipmentService.selectEquipmentList(
            new LockEquipmentParamVO());
        return success(list);
    }

    @PostMapping("/add")
    public AjaxResult add(@RequestBody LockEquipmentAddParamVO lockEquipmentAddParamVO) {
        lockEquipmentService.judgeName(lockEquipmentAddParamVO.getId(),
            lockEquipmentAddParamVO.getName(), lockEquipmentAddParamVO.getCabinetId());
        CommonUtils.addCommonParams(lockEquipmentAddParamVO, lockEquipmentAddParamVO.getId());
        return toAjax(lockEquipmentService.add(lockEquipmentAddParamVO));
    }

    @PostMapping("/update")
    public AjaxResult update(@RequestBody LockEquipmentAddParamVO lockEquipmentAddParamVO) {
        lockEquipmentService.judgeName(lockEquipmentAddParamVO.getId(),
            lockEquipmentAddParamVO.getName(), lockEquipmentAddParamVO.getCabinetId());
        CommonUtils.addCommonParams(lockEquipmentAddParamVO, lockEquipmentAddParamVO.getId());
        return toAjax(lockEquipmentService.update(lockEquipmentAddParamVO));
    }

    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(lockEquipmentService.deleteByIds(ids));
    }


    @PostMapping("/setTrust")
    public AjaxResult setTrust(@RequestBody LockEquipmentAddParamVO lockEquipmentAddParamVO){
        return toAjax(lockEquipmentService.setTrust(lockEquipmentAddParamVO));
    }
}
