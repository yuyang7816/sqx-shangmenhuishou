package com.sqx.app.index.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author chengming
 * @since 2025/9/28
 */
@Data
public class OrderStatisticsVo {

    @Schema(description = "总订单数")
    private Long totalCount;

    @Schema(description = "统计列表")
    private List<OrderStatistics> orderStatisticsList;

    @Data
    public static class OrderStatistics {
        private Integer status;
        private String desc;
        private Long count;
    }
}
