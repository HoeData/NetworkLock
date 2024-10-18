package com.ruoyi.web.enums;

import com.ruoyi.web.domain.vo.equipment.LockEquipmentParamVO;
import java.lang.reflect.Field;
import java.util.Objects;
import lombok.Getter;

@Getter
public enum QueryLatitudeType {
    COMPANY("company", "companyId"),
    SITE("site", "siteId"),
    MACHINE_ROOM("machineRoom", "machineRoomId"),
    CABINET("cabinet", "cabinetId"),
    ;
    private String type;
    private String fieldName;


    private QueryLatitudeType(String type, String fieldName) {
        this.type = type;
        this.fieldName = fieldName;
    }

    public static QueryLatitudeType getEnum(String key) {
        for (QueryLatitudeType type : QueryLatitudeType.values()) {
            if (Objects.equals(type.type, key)) {
                return type;
            }
        }
        throw new NullPointerException("请求参数错误");
    }
    public static void setField(LockEquipmentParamVO lockCommonParamVO, String type, Integer value) {
        QueryLatitudeType queryLatitudeType=QueryLatitudeType.getEnum(type);
        try {
            Class<?> cla = lockCommonParamVO.getClass();
            Field nameField = cla.getDeclaredField(queryLatitudeType.fieldName);
            nameField.setAccessible(true);
            nameField.set(lockCommonParamVO, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
