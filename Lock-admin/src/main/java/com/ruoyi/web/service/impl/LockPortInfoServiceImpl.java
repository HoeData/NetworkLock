package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.web.annotation.CompanyScope;
import com.ruoyi.web.domain.LockPortInfo;
import com.ruoyi.web.domain.vo.LockCommonParamVO;
import com.ruoyi.web.domain.vo.MonitorPortViewVO;
import com.ruoyi.web.domain.vo.port.LockPortInfoListParamVO;
import com.ruoyi.web.domain.vo.port.LockPortInfoStatisticalQuantityVO;
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
    @CompanyScope
    public LockPortInfoStatisticalQuantityVO getStatisticalQuantity(LockCommonParamVO vo) {
        LockPortInfoStatisticalQuantityVO lockPortInfoStatisticalQuantityVO = new LockPortInfoStatisticalQuantityVO();
        lockPortInfoStatisticalQuantityVO.setPortTotal(lockPortInfoMapper.selectPortTotal(vo));
        lockPortInfoStatisticalQuantityVO.setLockPortTotal(
            lockPortInfoMapper.selectLockPortTotal(vo));
        lockPortInfoStatisticalQuantityVO.setConsoleTotal(lockEquipmentMapper.selectConsoleTotal(vo));
        lockPortInfoStatisticalQuantityVO.setUseTotal(lockPortInfoMapper.selectUseTotal(vo));
        lockPortInfoStatisticalQuantityVO.setNoUseTotal(lockPortInfoMapper.selectNoUseTotal(vo));
        return lockPortInfoStatisticalQuantityVO;
    }

    @Override
    public List<MonitorPortViewVO> getMonitorPortList() {
        return lockPortInfoMapper.selectonitorPortList();
    }

    @Override
    @CompanyScope
    public List<LockPortInfo> getAll(LockPortInfoListParamVO portInfoListParamVO) {
        return lockPortInfoMapper.selectAllList(portInfoListParamVO);
    }

    @Override
    public void judgeUserCode(Integer id,String userCode) {
        LambdaQueryWrapper<LockPortInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LockPortInfo::getUserCode, userCode);
        wrapper.eq(LockPortInfo::getDelFlag, 0);
        LockPortInfo old = getOne(wrapper);
        if (null != old && null == id) {
            throw new ServiceException("该锁已被绑定,请先解绑,在进行绑定");
        }
        if (null != old && !old.getId().equals(id)) {
            throw new ServiceException("该锁已被绑定,请先解绑,在进行绑定");
        }
    }

    @Override
    public void saveOrUpdateForSynchronization(List<LockPortInfo> list) {
        saveOrUpdateBatch(list);
    }
}
