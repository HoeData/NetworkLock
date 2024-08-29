package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.domain.LockInfo;
import com.ruoyi.web.domain.LockPortInfo;
import com.ruoyi.web.domain.vo.LockPortInfoListParamVO;
import com.ruoyi.web.domain.vo.LockPortInfoStatisticalQuantityVO;
import com.ruoyi.web.domain.vo.MonitorPortViewVO;
import com.ruoyi.web.mapper.LockEquipmentMapper;
import com.ruoyi.web.mapper.LockPortInfoMapper;
import com.ruoyi.web.service.ILockPortInfoService;
import com.ruoyi.web.utils.LockUtil;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
        lockPortInfoStatisticalQuantityVO.setLockPortTotal(
            lockPortInfoMapper.selectLockPortTotal());
        lockPortInfoStatisticalQuantityVO.setConsoleTotal(lockEquipmentMapper.selectConsoleTotal());
        lockPortInfoStatisticalQuantityVO.setUseTotal(lockPortInfoMapper.selectUseTotal());
        lockPortInfoStatisticalQuantityVO.setNoUseTotal(lockPortInfoMapper.selectNoUseTotal());
        return lockPortInfoStatisticalQuantityVO;
    }

    @Override
    public String getHexMessageForAddLock(List<LockPortInfo> list) {
        List<LockInfo> lockInfoList = new ArrayList<>();
        list.forEach(lockPortInfo -> {
            lockPortInfo.setLockStatus(1);
            updateById(lockPortInfo);
            LockInfo lockInfo = new LockInfo();
            lockInfo.setLockNumber(
                (byte) Integer.parseInt(Integer.toHexString(lockPortInfo.getSerialNumber()), 16));
            lockInfo.setLockSerialNumber(
                lockPortInfo.getUserCode().getBytes(StandardCharsets.US_ASCII));
            lockInfo.setLockEffective(
                (byte) Integer.parseInt(Integer.toHexString(lockPortInfo.getValidityPeriod()), 16));
            lockInfo.setLockTime(
                (byte) Integer.parseInt(Integer.toHexString(lockPortInfo.getActionDuration()), 16));
            lockInfoList.add(lockInfo);
        });
        return LockUtil.bytesToHexWithSpaces(LockUtil.getByteForAddLock(lockInfoList));
    }

    @Override
    public String getHexMessageForDelLock(List<LockPortInfo> list) {
        List<LockInfo> lockInfoList = new ArrayList<>();
        list.forEach(lockPortInfo -> {
            lockPortInfo.setLockStatus(0);
            updateById(lockPortInfo);
            LockInfo lockInfo = new LockInfo();
            lockInfo.setLockNumber(
                (byte) Integer.parseInt(Integer.toHexString(lockPortInfo.getSerialNumber()), 16));
            lockInfoList.add(lockInfo);
        });
        return LockUtil.bytesToHexWithSpaces(LockUtil.getByteForDelLock(lockInfoList));
    }

    @Override
    public List<MonitorPortViewVO> getMonitorPortList() {
        return lockPortInfoMapper.selectonitorPortList();
    }
}
