package com.ruoyi.web.domain.vo.pda;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.page.PageDomain;
import java.util.Date;
import lombok.Data;

@Data
public class LockPdaDataSynchronizationInfoPageParamVO extends PageDomain {

    private Integer id;
    private Integer pdaId;
    private Integer status;
    private Integer type;
}
