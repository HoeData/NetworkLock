package com.ruoyi.web.domain.vo.pda;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.web.domain.vo.PageVO;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UnlockPageParamVO extends PageVO {

    private Integer successFlag;
    private String userName;
    private Integer status;
    private String lockSerialNumber;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime queryStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime queryEndDate;
    private Integer pdaUserId;
}
