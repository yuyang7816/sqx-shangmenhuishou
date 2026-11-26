package com.sqx.app.base.pay.enums;

/**
 * @author chengming
 * @since 2025/4/22
 */
public enum WxTradeType {

    JSAPI("JSAPI", "公众号支付"),
    NATIVE("NATIVE", "原生扫码支付"),
    APP("APP", "app支付"),
    ;

    private final String code;
    private final String desc;

    WxTradeType(String code, String desc) {
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
