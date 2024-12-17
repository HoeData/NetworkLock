package com.manniu.screen.vo.param;

import com.manniu.screen.vo.PageVO;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LockScreenUserLockPermissionsPageParamVO extends PageVO {
    @NotNull(message = "用户不能为空")
    private Integer userId;
    private Integer authorizationFlag;
}
