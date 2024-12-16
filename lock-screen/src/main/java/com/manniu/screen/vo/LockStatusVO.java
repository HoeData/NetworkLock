package com.manniu.screen.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LockStatusVO {

    private String doorStatus;
    private String lockStatus;
    private String onlineStatus;
}
