package com.sqx.app.waste.item.domain.vo;

import com.sqx.app.waste.item.domain.entity.WasteItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-28 15:47:04
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WasteItemVo extends WasteItem{

    @Schema(description = "分类名称")
    private String categoryName;

    @Schema(description = "上级分类ID")
    private Long categoryParentId;
}
