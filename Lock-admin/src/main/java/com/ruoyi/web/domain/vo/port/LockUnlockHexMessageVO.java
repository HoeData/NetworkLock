package com.ruoyi.web.domain.vo.port;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LockUnlockHexMessageVO {

    @NotNull(message = "电子钥匙不能为空")
    private Integer pdaInfoId;
    private List<String> hexMessageList;
}
