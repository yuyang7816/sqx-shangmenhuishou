package com.sqx.app.waste.item.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-28 15:47:04
 */
@Data
public class WasteItemUpdateBo {

    @Schema(description = "id")
    private Long id;
    @Schema(description = "分类id")
    private Long categoryId;
    @Schema(description = "名称")
    private String name;
    @Schema(description = "封面图")
    private String cover;
    @Schema(description = "详情图，逗号分割")
    private String pic;
    @Schema(description = "标签，逗号分割")
    private String tags;
    @Schema(description = "预约数")
    private Integer appointmentNum;
    @Schema(description = "价格")
    private BigDecimal price;
    @Schema(description = "城市")
    private String city;
}
