package com.sqx.app.waste.order.order.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author chengming
 * @since 2025/9/18
 */
@Data
public class TakeOrderDataVo {

    @Schema(description = "总接单")
    private Long allCount;

    @Schema(description = "今日接单")
    private Long todayCount;

    @Schema(description = "月接单")
    private Long monthCount;
}
