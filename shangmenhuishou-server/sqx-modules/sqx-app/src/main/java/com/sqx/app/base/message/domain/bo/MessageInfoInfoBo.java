package com.sqx.app.base.message.domain.bo;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

/**
 * @author sqx
 * @since 2025-05-19 15:35:07
 */
@Data
public class MessageInfoInfoBo {
    @Schema(description = "消息id")
    @NotNull(message = "id不能为空")
    private Long id;

}
