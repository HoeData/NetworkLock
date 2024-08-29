package com.ruoyi.web.controller.lock;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.web.domain.LockMachineRoom;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.domain.vo.LockCommonViewVO;
import com.ruoyi.web.service.ILockMachineRoomService;
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
    public AjaxResult getAll() {
        List<LockCommonViewVO> list = lockMachineRoomService.selectMachineRoomList(
            new LockCommonParamVO());
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


}
