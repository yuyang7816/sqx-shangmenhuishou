package com.sqx.app.searchHistory.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author chengming
 * @since 2025/9/17
 */
@Data
public class SearchHistoryListBo {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "关键字")
    private String keyword;
}
