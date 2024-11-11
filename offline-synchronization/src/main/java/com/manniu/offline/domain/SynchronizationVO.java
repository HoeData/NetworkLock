package com.manniu.offline.domain;

import java.util.List;
import lombok.Data;

@Data
public class SynchronizationVO {
    private PdaMergeDataVO pdaMergeDataVO;
    private String deviceId;
    private List<String> licenseStrList;
    private int licenseMaxNumber;
}
