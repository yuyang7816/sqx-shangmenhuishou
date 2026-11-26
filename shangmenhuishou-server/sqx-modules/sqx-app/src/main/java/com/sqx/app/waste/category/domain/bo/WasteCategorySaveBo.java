package com.sqx.app.waste.category.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-03 11:36:52
 */
@Data
public class WasteCategorySaveBo {

    @Schema(description = "父类别ID。顶级大类为0")
    private Long parentId;

    @Schema(description = "类别名称")
    private String name;

    @Schema(description = "层级 (1: 大类, 2: 子类)")
    private Integer level;

    @Schema(description = "封面图")
    private String cover;

    @Schema(description = "详情图 逗号分割")
    private String pic;

    @Schema(description = "标签 逗号分割")
    private String tags;
}
