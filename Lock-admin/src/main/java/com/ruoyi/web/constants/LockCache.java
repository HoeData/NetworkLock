package com.ruoyi.web.constants;

import com.ruoyi.common.license.LicenseParamVO;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LockCache {

    public static Set<String> lockSerialNumberSet = new HashSet<>();

    public static int lockNumber;
    public static List<LicenseParamVO> licenseParamVOList = new ArrayList<>();

}
