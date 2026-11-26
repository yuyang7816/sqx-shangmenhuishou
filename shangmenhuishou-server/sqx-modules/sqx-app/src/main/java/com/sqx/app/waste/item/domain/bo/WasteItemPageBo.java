package com.sqx.app.waste.item.domain.bo;

import com.sqx.common.mybatis.domain.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-28 15:47:04
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WasteItemPageBo extends PageParam {

    @Schema(description = "分类id")
    private Long categoryId;

    @Schema(description = "分类名称")
    private String categoryName;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "城市")
    private String city;
}
