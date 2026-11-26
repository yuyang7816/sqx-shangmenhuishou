package com.sqx.app.waste.price.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-03 11:37:16
 */
@Data
public class WastePriceUpdateBo {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "废品类别ID (通常是子类)")
    private Long categoryId;

    @Schema(description = "单位（5-10kg、1件）")
    private String unit;

    @Schema(description = "价格")
    private BigDecimal price;

    @Schema(description = "排序序号")
    private Integer sort;
}
