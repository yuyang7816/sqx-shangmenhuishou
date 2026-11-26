package com.sqx.app.waste.order.order.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author chengming
 * @since 2025/9/30
 */
@Data
public class WasteOrderCancelBo {

    @Schema(description = "用户id", hidden = true)
    private Long userId;

    @Schema(description = "订单id")
    private Long orderId;
}
