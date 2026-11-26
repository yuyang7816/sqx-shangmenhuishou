package com.sqx.app.base.user.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author chengming
 * @since 2025/10/14
 */
@Data
public class UpdateUserDepositBO {

    @Schema(description = "用户id")
    @NotNull(message = "用户id不能为空")
    private Long userId;

    @Schema(description = "金额")
    @NotNull(message = "金额不能为空")
    private BigDecimal amount;

    @Schema(description = "操作类型 incr=增加 decr=减少")
    @NotBlank(message = "操作类型不能为空")
    private String operationType;
}
