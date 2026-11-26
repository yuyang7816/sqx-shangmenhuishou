package com.sqx.app.waste.order.item.domain.bo;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-03 14:28:51
 */
@Data
public class WasteOrderItemSaveBo {

    @Schema(description = "订单id")
    private Long orderId;

    @Schema(description = "废品类别id")
    private Long categoryId;

    @Schema(description = "废品类别名称")
    private String categoryName;

    @Schema(description = "废品子类id")
    private Long categoryChildrenId;

    @Schema(description = "废品子类名称")
    private String categoryChildrenName;

    @Schema(description = "单位")
    private String unit;

    @Schema(description = "预估价格")
    private String price;

}
