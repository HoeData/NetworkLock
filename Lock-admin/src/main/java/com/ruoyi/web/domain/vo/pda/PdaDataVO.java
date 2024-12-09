package com.ruoyi.web.domain.vo.pda;

import com.ruoyi.web.domain.LockPortInfo;
import com.ruoyi.web.domain.LockUnlockLog;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class PdaDataVO implements Serializable {

    private List<LockPortInfo> lockPortInfo = new ArrayList<>();
    private List<LockUnlockLog> lockUnlockLog = new ArrayList<>();
    private String pdaId;

}
