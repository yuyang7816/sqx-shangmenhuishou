package com.sqx.app.index.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author chengming
 * @since 2025/9/28
 */
@Data
public class UserStatisticsVo {

    @Schema(description = "总用户数")
    private Long userCount;

    @Schema(description = "普通用户数")
    private Long normalUserCount;

    @Schema(description = "回收员用户数")
    private Long wasteUserCount;
}
