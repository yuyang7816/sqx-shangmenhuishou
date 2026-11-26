package com.sqx.app.waste.price.domain.bo;

import com.sqx.common.mybatis.domain.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-03 11:37:16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WastePricePageBo extends PageParam {

    @Schema(description = "废品类别ID (通常是子类)")
    private Long categoryId;
}
