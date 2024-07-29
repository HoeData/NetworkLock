package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.domain.LockPortInfo;
import com.ruoyi.web.domain.vo.LockPortInfoListParamVO;
import com.ruoyi.web.domain.vo.LockPortInfoStatisticalQuantityVO;
import com.ruoyi.web.mapper.LockEquipmentMapper;
import com.ruoyi.web.mapper.LockPortInfoMapper;
import com.ruoyi.web.service.ILockPortInfoService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class LockPortInfoServiceImpl extends
    ServiceImpl<LockPortInfoMapper, LockPortInfo> implements ILockPortInfoService {

    @Resource
    private LockPortInfoMapper lockPortInfoMapper;
    @Resource
    private LockEquipmentMapper lockEquipmentMapper;

    @Override
    public void deleteByEquipmentIds(String[] ids) {
        lockPortInfoMapper.deleteByEquipmentIds(ids);
    }

    @Override
    public List<LockPortInfo> selectPortInfoList(LockPortInfoListParamVO portInfoListParamVO) {
        return lockPortInfoMapper.selectPortInfoList(portInfoListParamVO);
    }

    @Override
    public LockPortInfoStatisticalQuantityVO getStatisticalQuantity() {
        LockPortInfoStatisticalQuantityVO lockPortInfoStatisticalQuantityVO = new LockPortInfoStatisticalQuantityVO();
        lockPortInfoStatisticalQuantityVO.setPortTotal(lockPortInfoMapper.selectPortTotal());
        lockPortInfoStatisticalQuantityVO.setLockPortTotal(lockPortInfoMapper.selectLockPortTotal());
        lockPortInfoStatisticalQuantityVO.setConsoleTotal(lockEquipmentMapper.selectConsoleTotal());
        lockPortInfoStatisticalQuantityVO.setUseTotal(lockPortInfoMapper.selectUseTotal());
        lockPortInfoStatisticalQuantityVO.setNoUseTotal(lockPortInfoMapper.selectNoUseTotal());
        return lockPortInfoStatisticalQuantityVO;
    }

}
