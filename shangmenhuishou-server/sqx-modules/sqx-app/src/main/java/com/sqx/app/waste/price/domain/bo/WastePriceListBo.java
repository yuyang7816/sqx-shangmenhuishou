package com.sqx.app.waste.price.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author chengming
 * @since 2025/9/28
 */
@Data
public class WastePriceListBo {

    @Schema(description = "废品类别ID (通常是子类)")
    private Long categoryId;
}
