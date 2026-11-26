package com.sqx.app.base.user.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author chengming
 * @since 2025/9/1
 */
@Data
public class RefundDepositBo {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "金额")
    private BigDecimal amount;
}
