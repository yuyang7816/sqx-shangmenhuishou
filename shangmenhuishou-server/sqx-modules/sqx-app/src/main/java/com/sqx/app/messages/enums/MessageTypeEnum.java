package com.sqx.app.messages.enums;

/**
 * @author chengming
 * @since 2025/11/19
 */
public enum MessageTypeEnum {
    NOTICE(1, "系统公告"),
    USER(2, "用户消息"),
    NOTIFY(3, "系统通知"),
    ;

    private final Integer code;
    private final String desc;

    MessageTypeEnum(Integer code, String desc) {
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
