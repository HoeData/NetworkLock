package com.manniu.screen.vo.param;

import com.manniu.screen.vo.PageVO;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LockScreenUserLockPermissionsPageParamVO extends PageVO {

    private String carNumber;
    private String electronicLockSerialNumber;
    @NotNull(message = "用户不能为空")
    private Long userId;
}
