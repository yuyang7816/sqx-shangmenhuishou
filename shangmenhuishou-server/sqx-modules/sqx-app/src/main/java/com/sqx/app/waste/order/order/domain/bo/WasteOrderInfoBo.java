package com.sqx.app.waste.order.order.domain.bo;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

/**
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-03 14:28:34
 */
@Data
public class WasteOrderInfoBo {

    @Schema(description = "订单id")
    @NotNull(message = "id不能为空")
    private Long id;

}
