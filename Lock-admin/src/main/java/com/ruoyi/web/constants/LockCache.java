package com.ruoyi.web.constants;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ruoyi.common.license.LicenseParamVO;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LockCache {

    public static Set<String> lockSerialNumberSet = Sets.newHashSet();
    public static int lockNumber;
    public static List<LicenseParamVO> licenseParamVOList = Lists.newArrayList();
    public static List<String> licenseStrList =  Lists.newArrayList();

}
