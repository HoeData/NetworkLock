package com.ruoyi.web.aspectj;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.web.annotation.CompanyScope;
import com.ruoyi.web.constants.CommonConst;
import com.ruoyi.web.domain.LockCompany;
import com.ruoyi.web.domain.vo.LockEntity;
import com.ruoyi.web.service.ILockCompanyService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class CompanyScopeAspect {

    public static final String COMPANY_SCOPE = "companyScope";

    @Before("@annotation(controllerCompanyScope)")
    public void doBefore(JoinPoint point, CompanyScope controllerCompanyScope) throws Throwable {
        handleDataScope(point, controllerCompanyScope);
    }

    protected void handleDataScope(final JoinPoint joinPoint, CompanyScope controllerCompanyScope) {
        // 获取当前的用户
        try {
            LoginUser loginUser = SecurityUtils.getLoginUser();
            if (StringUtils.isNotNull(loginUser)) {
                SysUser currentUser = loginUser.getUser();
                // 如果是超级管理员，则不过滤数据
                if (StringUtils.isNotNull(currentUser) && !currentUser.isAdmin()) {
                    companyScopeFilter(joinPoint, currentUser,
                        controllerCompanyScope.companyAlias());
                }
            }
        } catch (Throwable e) {
            log.error("CompanyScopeAspect-handleDataScope" + e.getMessage());
        }

    }

    /**
     * 数据范围过滤
     *
     * @param joinPoint    切点
     * @param user         用户
     * @param companyAlias 公司别名
     */
    public static void companyScopeFilter(JoinPoint joinPoint, SysUser user, String companyAlias) {
        LockCompany lockCompany = SpringUtils.getBean(ILockCompanyService.class)
            .getByIdCache(user.getCompanyId());
        if (StringUtils.isNotBlank(companyAlias)) {
            companyAlias = companyAlias + CommonConst.POINT;
        }
        String sqlString =
            " " + companyAlias + "id in (select id from lock_company where path like '%"
                + lockCompany.getPath() + "')";
        if (StringUtils.isNotBlank(sqlString)) {
            Object params = joinPoint.getArgs()[0];
            if (StringUtils.isNotNull(params) && params instanceof LockEntity) {
                LockEntity lockEntity = (LockEntity) params;
                lockEntity.getParamMap().put(COMPANY_SCOPE, sqlString);
            }
        }
    }
}
