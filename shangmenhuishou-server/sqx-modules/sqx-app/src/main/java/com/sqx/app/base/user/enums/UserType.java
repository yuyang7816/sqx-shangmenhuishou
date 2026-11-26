package com.sqx.app.base.user.enums;

/**
 * @author chengming
 * @since 2025/10/14
 */
public enum UserType {
    NORMAL(1, "普通用户"),
    WASTE(2, "回收员"),
    ;

    private final Integer code;
    private final String desc;

    UserType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
