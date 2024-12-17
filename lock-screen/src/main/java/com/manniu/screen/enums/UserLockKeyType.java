package com.manniu.screen.enums;

import java.util.Objects;

public enum UserLockKeyType {

    CARD_NUMBER(1), PASSWORD(2), FINGERPRINT(3);
    private Integer value;

    UserLockKeyType(Integer value) {
        this.value = value;
    }

    public static UserLockKeyType getEnum(Integer value) {
        for (UserLockKeyType type : UserLockKeyType.values()) {
            if (Objects.equals(type.value, value)) {
                return type;
            }
        }
        throw new NullPointerException("类型不能为空");
    }
}
