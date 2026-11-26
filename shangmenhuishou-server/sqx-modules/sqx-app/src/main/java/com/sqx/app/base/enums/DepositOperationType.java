package com.sqx.app.base.enums;

/**
 * @author chengming
 * @since 2025/10/14
 */
public enum DepositOperationType {
    incr("incr", "增加"),
    decr("decr", "减少"),
    ;

    private final String code;
    private final String desc;

    DepositOperationType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
