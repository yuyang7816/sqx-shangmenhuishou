package com.sqx.app.base.auth.enums;

import com.sqx.common.core.exception.BizException;

/**
 * @author chengming
 * @since 2025/11/18
 */
public enum ClientEnum {
    USER("user", "用户端"),
    WASTE("waste", "回收员端")
    ;

    private final String code;
    private final String desc;

    ClientEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static ClientEnum fromCode(String code) {
        for (ClientEnum client : ClientEnum.values()) {
            if (client.code.equals(code)) {
                return client;
            }
        }

        throw new BizException("不支持的客户端");
    }
}
