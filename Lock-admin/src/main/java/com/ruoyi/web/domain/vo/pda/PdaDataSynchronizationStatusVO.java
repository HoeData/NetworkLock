package com.ruoyi.web.domain.vo.pda;

import java.io.Serializable;
import lombok.Data;

/**
 * pda数据同步vo pda首先创建需要同步数据，此时pdaCreateDataFlag为false; pda创建好数据后，此时pdaCreateDataFlag为false为true；
 * PC端接受到true，开始获取数据，此时pcGetDataFlag为false，pc端获取数据后，pcGetDataFlag为true，此时pcCreateDataFlag为false;
 * pc合并完数据后，pcCreateDataFlag为true，pda端获取数据pdaGetDataFlag为false,pda端获取完数据后，pdaGetDataFlag为true;
 * 同步将将endFlag修改为true;
 *
 * @author xiaomi
 * @date 2024/09/02
 */
@Data
public class PdaDataSynchronizationStatusVO implements Serializable {

    private Boolean readyFlag = false;
    private Boolean pdaCreateDataFlag = false;
    private Boolean pcGetDataFlag = false;
    private Boolean pcCreateDataFlag = false;
    private Boolean pdaGetDataFlag = false;
    private Boolean endFlag = false;
    private Boolean pdaAuthorizationFlag = false;
    private Boolean pdaResetPasswordFlag = false;
}
