package com.ruoyi.web.controller.lock;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.web.domain.LockEquipmentType;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.service.ILockEquipmentTypeService;
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
        List<LockEquipmentType> list = lockEquipmentTypeService.selectEquipmentTypeList(new LockCommonParamVO());
        return success(list);
    }

    @PostMapping("/saveOrUpdate")
    public AjaxResult saveOrUpdate(@RequestBody LockEquipmentType lockEquipmentType) {
        CommonUtils.addCommonParams(lockEquipmentType, lockEquipmentType.getId());
        return toAjax(lockEquipmentTypeService.saveOrUpdate(lockEquipmentType));
    }

    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(lockEquipmentTypeService.deleteByIds(ids));
    }


}
