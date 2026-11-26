package com.sqx.app.deposit.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-10-13 14:47:55
 */
@Data
public class DepositRecordSaveBo {
    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "金额")
    private BigDecimal amount;

    @Schema(description = "资金流向: 1=收入, 2=支出")
    private Integer direction;
}
