package com.sqx.app.waste.category.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author chengming
 * @since 2025/9/28
 */
@Data
public class WasteCategoryListBo {

    @Schema(description = "父类别ID。顶级大类为0")
    private Long parentId;

    @Schema(description = "类别名称")
    private String name;

    @Schema(description = "层级 (1: 大类, 2: 子类)")
    private Integer level;
}
