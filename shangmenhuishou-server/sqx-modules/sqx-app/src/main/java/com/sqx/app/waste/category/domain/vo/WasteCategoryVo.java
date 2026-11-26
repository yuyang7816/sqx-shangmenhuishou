package com.sqx.app.waste.category.domain.vo;

import com.sqx.app.waste.category.domain.entity.WasteCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-03 11:36:52
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WasteCategoryVo extends WasteCategory {

    @Schema(description = "子类别")
    private List<WasteCategory> children;
}
