package com.sqx.app.referral.withdraw.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author chengming
 * @since 2025/9/4
 */
@Data
public class WithdrawStatisticsVo {

    @Schema(description = "累计提现金额")
    private BigDecimal totalAmount;

    @Schema(description = "累计提现次数")
    private Long totalCount;

    @Schema(description = "待转帐次数")
    private Long waitCount;
}
