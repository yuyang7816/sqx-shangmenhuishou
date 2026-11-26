package com.sqx.app.waste.item.domain.bo;

import com.sqx.common.mybatis.domain.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author chengming
 * @since 2025/10/16
 */
@Data
public class WasteItemPageByCategoryIdBo extends PageParam {

    @Schema(description = "大类id")
    private Long categoryId;
}
