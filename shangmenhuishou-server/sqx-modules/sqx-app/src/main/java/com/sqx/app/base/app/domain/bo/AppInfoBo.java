package com.sqx.app.base.app.domain.bo;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

/**
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-11-18 15:55:50
 */
@Data
public class AppInfoBo {

    @Schema(description = "")
    @NotNull(message = "id不能为空")
    private Long id;

}
