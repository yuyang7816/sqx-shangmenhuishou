package com.sqx.app.index.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author chengming
 * @since 2025/10/14
 */
@Data
public class IndexStatisticsVo {

    @Schema(description = "总用户数")
    private Long userCount;

    @Schema(description = "今日新增用户数")
    private Long todayUserCount;

    @Schema(description = "本月新增用户数")
    private Long monthUserCount;

    @Schema(description = "本年新增用户数")
    private Long yearUserCount;

    @Schema(description = "普通用户数")
    private Long normalUserCount;

    @Schema(description = "回收员用户数")
    private Long wasteUserCount;

    @Schema(description = "总订单数")
    private Long orderCount;

    @Schema(description = "今日订单数")
    private Long dayOrderCount;

    @Schema(description = "本月订单数")
    private Long monthOrderCount;

    @Schema(description = "本年订单数")
    private Long yearOrderCount;

    @Schema(description = "总预估交易金额")
    private BigDecimal amount;

    @Schema(description = "今日预估交易金额")
    private BigDecimal dayAmount;

    @Schema(description = "本月预估交易金额")
    private BigDecimal monthAmount;

    @Schema(description = "本年预估交易金额")
    private BigDecimal yearAmount;
}
