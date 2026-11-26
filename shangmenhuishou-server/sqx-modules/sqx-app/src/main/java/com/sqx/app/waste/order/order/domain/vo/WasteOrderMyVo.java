package com.sqx.app.waste.order.order.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chengming
 * @since 2025/9/17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WasteOrderMyVo {

    @Schema(description = "订单状态：0=待接单；1=待服务；2=进行中；3=已完成；4=已取消；")
    private Integer status;

    @Schema(description = "数量")
    private Long count;
}
