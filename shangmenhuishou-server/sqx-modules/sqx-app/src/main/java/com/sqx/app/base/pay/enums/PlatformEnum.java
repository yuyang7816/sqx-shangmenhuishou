package com.sqx.app.base.pay.enums;

/**
 * @author chengming
 * @since 2025/4/22
 */
public enum PlatformEnum {
    ALI("ali", "支付宝"),
    WX("wx", "微信"),
    PAYPAL("paypal", "paypal"),
    STRIPE("stripe", "stripe"),
    ;

    private final String code;
    private final String desc;

    PlatformEnum(String code, String desc) {
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
