package com.sqx.app.waste.order.order.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author chengming
 * @since 2025/9/30
 */
@Data
public class WasteOrderInformationBo {

    @Schema(description = "id")
    @NotNull(message = "id不能为空")
    private Long id;

    @Schema(description = "纬度")
    @NotNull(message = "纬度不能为空")
    private BigDecimal lat;

    @Schema(description = "经度")
    @NotNull(message = "经度不能为空")
    private BigDecimal lng;
}
