package com.sqx.app.address.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author chengming
 * @since 2025/9/17
 */
@Data
public class AddressUpdateDefaultBo {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "地址id")
    private Long id;

    @Schema(description = "是否默认 1=默认 2=非默认")
    private Integer isDefault;
}
