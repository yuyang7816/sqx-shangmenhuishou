package com.sqx.app.base.pay.enums;

/**
 * @author chengming
 * @since 2025/4/21
 * 支付业务类型
 */
public enum BusinessType {
    deposit(1, "缴纳保证金")
    ;

    private final Integer code;
    private final String desc;

    BusinessType(Integer code, String desc) {
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
