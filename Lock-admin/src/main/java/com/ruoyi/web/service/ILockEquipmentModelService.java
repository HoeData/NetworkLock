package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.web.domain.LockEquipmentModel;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import java.util.List;

public interface ILockEquipmentModelService extends IService<LockEquipmentModel> {


    List<LockEquipmentModel> selectEquipmentModelList(LockCommonParamVO lockCommonParamVO);

    int deleteByIds(String[] ids);

    void judgeName(LockEquipmentModel lockEquipmentModel);

    List<LockEquipmentModel> getAll();
}
