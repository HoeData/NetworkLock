package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.web.domain.LockAuthorizationLog;
import com.ruoyi.web.domain.RelPdaUserPort;
import com.ruoyi.web.domain.vo.pda.RelPdaUserPortParamVO;
import com.ruoyi.web.domain.vo.pda.RelPdaUserPortViewVO;
import com.ruoyi.web.mapper.RelPdaUserPortMapper;
import com.ruoyi.web.service.ILockAuthorizationLogService;
import com.ruoyi.web.service.IRelPdaUserPortService;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RelPdaUserPortServiceImpl extends
    ServiceImpl<RelPdaUserPortMapper, RelPdaUserPort> implements IRelPdaUserPortService {

    private final RelPdaUserPortMapper pdaUserPortMapper;
    private final ILockAuthorizationLogService lockAuthorizationLogService;

    @Override
    public List<RelPdaUserPortViewVO> getAllList(RelPdaUserPortParamVO vo) {
        List<RelPdaUserPortViewVO> list = pdaUserPortMapper.selectAllList(vo);
        return list;
    }

    @Override
    public List<RelPdaUserPortViewVO> getAuthorizationList(RelPdaUserPortParamVO vo) {
        List<RelPdaUserPortViewVO> list = pdaUserPortMapper.selectAuthorizationList(vo);
        return list;
    }

    @Override
    @Transactional
    public int saveAuthorization(List<RelPdaUserPort> list) {
        LockAuthorizationLog lockAuthorizationLog = new LockAuthorizationLog();
        lockAuthorizationLog.setPdaUserId(list.get(0).getPdaUserId());
        lockAuthorizationLog.setCreateTime(new Date());
        lockAuthorizationLog.setCreateBy(SecurityUtils.getLoginUser().getUserId().toString());
        lockAuthorizationLog.setSuccessFlag(0);
        lockAuthorizationLogService.save(lockAuthorizationLog);
        try {
            pdaUserPortMapper.deleteByPdaUserId(list.get(0).getPdaUserId());
            list.forEach(item -> item.setCreateTime(new Date()));
            return saveBatch(list) ? 1 : 0;
        } catch (Throwable t) {
            lockAuthorizationLog.setErrorMsg(
                StringUtils.isNotBlank(t.getMessage()) ? t.getMessage().substring(0,200) : "未知错误");
            lockAuthorizationLog.setSuccessFlag(1);
            lockAuthorizationLogService.updateById(lockAuthorizationLog);
            throw new ServiceException("授权失败");
        }

    }
}
