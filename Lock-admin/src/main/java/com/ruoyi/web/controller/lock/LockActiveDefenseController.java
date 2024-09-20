package com.ruoyi.web.controller.lock;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.web.domain.vo.equipment.ActiveDefenseSaveOrUpdateParamVO;
import com.ruoyi.web.domain.vo.equipment.LockEquipmentParamVO;
import com.ruoyi.web.domain.vo.equipment.LockEquipmentViewVO;
import com.ruoyi.web.service.ILockEquipmentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lock/activeDefense")
@RequiredArgsConstructor
public class LockActiveDefenseController extends BaseController {

    private final ILockEquipmentService lockEquipmentService;

    @PostMapping("/list")
    public TableDataInfo list(@RequestBody LockEquipmentParamVO lockEquipmentParamVO) {
        lockEquipmentParamVO.setActiveDefenseFlag(1);
        PageHelper.startPage(lockEquipmentParamVO.getPageNum(), lockEquipmentParamVO.getPageSize());
        List<LockEquipmentViewVO> list = lockEquipmentService.selectEquipmentList(
            lockEquipmentParamVO);
        return getDataTable(list);
    }

    @PostMapping("/add")
    public AjaxResult add(@RequestBody ActiveDefenseSaveOrUpdateParamVO vo) {
        return toAjax(lockEquipmentService.setActiveDefenseFlag(vo));
    }

    @PostMapping("/update")
    public AjaxResult update(@RequestBody ActiveDefenseSaveOrUpdateParamVO vo) {
        return toAjax(lockEquipmentService.updateActiveDefenseFlag(vo));
    }

    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(lockEquipmentService.removeActiveDefenseByIds(ids));
    }
}
