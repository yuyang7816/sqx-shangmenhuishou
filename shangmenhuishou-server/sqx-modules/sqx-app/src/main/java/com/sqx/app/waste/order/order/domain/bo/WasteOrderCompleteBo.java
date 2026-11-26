package com.sqx.app.waste.order.order.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author chengming
 * @since 2025/10/13
 */
@Data
public class WasteOrderCompleteBo {

    @Schema(description = "回收员id")
    private Long userId;

    @Schema(description = "订单id")
    private Long orderId;
}
