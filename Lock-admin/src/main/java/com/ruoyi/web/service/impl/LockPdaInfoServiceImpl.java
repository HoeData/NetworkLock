package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.web.annotation.CompanyScope;
import com.ruoyi.web.constants.PdaUserConst;
import com.ruoyi.web.domain.LockPdaInfo;
import com.ruoyi.web.domain.LockPdaUser;
import com.ruoyi.web.domain.vo.pda.LockPadPageParamVO;
import com.ruoyi.web.mapper.LockPdaInfoMapper;
import com.ruoyi.web.service.ILockPdaInfoService;
import com.ruoyi.web.service.ILockPdaUserService;
import com.ruoyi.web.utils.CommonUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LockPdaInfoServiceImpl extends ServiceImpl<LockPdaInfoMapper, LockPdaInfo> implements
    ILockPdaInfoService {

    private final LockPdaInfoMapper pdaInfoMapper;
    private final ILockPdaUserService pdaUserService;

    @Override
    public void judgeKey(LockPdaInfo lockPdaInfo) {
        LockPdaInfo old = getByKey(lockPdaInfo.getOnlyKey());
        if (null != old && null == lockPdaInfo.getId()) {
            throw new ServiceException("数据已存在");
        }
        if (null != old && !old.getId().equals(lockPdaInfo.getId())) {
            throw new ServiceException("数据已存在");
        }
    }

    @Override
    @CompanyScope(companyAlias = "company")
    public List<LockPdaInfo> getPdaInfoList(LockPadPageParamVO padPageParamVO) {
        return pdaInfoMapper.selectPdaInfoList(padPageParamVO);
    }

    @Override
    public int deleteByIds(String[] ids) {
        pdaInfoMapper.deleteByIds(ids);
        return pdaUserService.delByPdaId(ids);
    }

    @Override
    public LockPdaInfo getByKey(String key) {
        LambdaQueryWrapper<LockPdaInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LockPdaInfo::getOnlyKey, key);
        wrapper.eq(LockPdaInfo::getDelFlag, 0);
        return getOne(wrapper);
    }

    @Override
    @Transactional
    public int saveOrUpdateAll(LockPdaInfo lockPdaInfo) {
        if (null == lockPdaInfo.getId()) {
            save(lockPdaInfo);
            LockPdaUser lockPdaUser = new LockPdaUser();
            lockPdaUser.setPdaId(lockPdaInfo.getId());
            lockPdaUser.setUserName(PdaUserConst.DEFAULT_USER_NAME);
            lockPdaUser.setAdminFlag(0);
            lockPdaUser.setDescription(PdaUserConst.DEFAULT_USER_DESCRIPTION);
            lockPdaUser.setPassword("");
            CommonUtils.addCommonParams(lockPdaUser, null);
            return pdaUserService.save(lockPdaUser) ? 1 : 0;
        }
        return updateById(lockPdaInfo) ? 1 : 0;
    }
}
