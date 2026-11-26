package com.sqx.app.waste.order.order.enums;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author chengming
 * @since 2025/9/3
 */
public enum OrderStatus {
    /**
     * 订单状态：0=待接单；1=待服务；2=进行中；3=已完成；4=已取消；
     */
    /**
     * 待接单：订单已创建，等待服务人员接单
     */
    PENDING_ACCEPT(0, "待接单"),

    /**
     * 待服务：订单已被接单，等待服务开始
     */
    PENDING_SERVICE(1, "待服务"),

    /**
     * 进行中：服务已经开始，正在进行中
     */
    IN_PROGRESS(2, "进行中"),

    /**
     * 已完成：服务已成功完成
     */
    COMPLETED(3, "已完成"),

    /**
     * 已取消：订单已被用户或系统取消
     */
    CANCELLED(4, "已取消"),
    ;

    private final Integer code;
    private final String message;

    OrderStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    // 缓存所有code
    public static final List<Integer> ALL_CODES;

    static {
        List<Integer> list = new ArrayList<>();
        for (OrderStatus orderStatus : OrderStatus.values()) {
            Integer orderStatusCode = orderStatus.getCode();
            list.add(orderStatusCode);
        }

        ALL_CODES = Collections.unmodifiableList(list);
    }

    // 根据 code 获取描述
    public static String getDescByCode(Integer code) {
        for (OrderStatus status : values()) {
            if (Objects.equals(status.getCode(), code)) {
                return status.getMessage();
            }
        }

        throw new IllegalArgumentException("错误的订单状态: " + code);
    }
}
