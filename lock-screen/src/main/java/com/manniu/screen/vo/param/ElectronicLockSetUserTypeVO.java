package com.manniu.screen.vo.param;

import com.manniu.screen.vo.CommonVO;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ElectronicLockSetUserTypeVO extends CommonVO {

    @NotNull(message = "锁ID不能为空")
    private Integer id;
    @NotBlank(message = "单双用户验证不能为空")
    private String userType;
}
