package com.manniu.screen.config;

import com.manniu.screen.vo.LockStatusVO;
import java.util.HashMap;
import java.util.Map;

public class LockScreenCache {

    public static boolean init = false;
    /**
     * 锁定状态vo map key为设备ID value map的key为锁ID
     */

    public static Map<String, Map<String, LockStatusVO>> lockStatusVOMap = new HashMap<>();
    public static Map<String, String> deviceStatusVOMap = new HashMap<>();
    public static Map<String, String> doorStatus = new HashMap<>(2);
    public static Map<String, String> lockStatus = new HashMap<>(2);
    public static Map<String, String> codeMap = new HashMap<>(14);

    static {
        doorStatus.put("0", "关");
        doorStatus.put("1", "开 ");
        lockStatus.put("0", "关");
        lockStatus.put("1", "开 ");
        codeMap.put("01", "远程开锁成功");
        codeMap.put("02", "刷卡开锁成功");
        codeMap.put("03", "蓝牙开锁成功");
        codeMap.put("04", "开关量开锁成功");
        codeMap.put("05", "指纹开锁成功");
        codeMap.put("06", "密码开锁成功");
        codeMap.put("10", "锁关闭");
        codeMap.put("11", "锁开启");
        codeMap.put("82", "无效卡开锁失败");
        codeMap.put("83", "卡无效时间段开锁失败");
        codeMap.put("84", "无效密码开锁失败");
        codeMap.put("85", "密码无效时间段开锁失败");
        codeMap.put("86", "无效指纹开锁失败");
        codeMap.put("87", "指纹无效时间段开锁失败");
    }

}
