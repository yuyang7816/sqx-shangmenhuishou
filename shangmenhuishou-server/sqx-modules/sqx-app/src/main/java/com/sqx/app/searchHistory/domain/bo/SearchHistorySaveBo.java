package com.sqx.app.searchHistory.domain.bo;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-17 13:23:01
 */
@Data
public class SearchHistorySaveBo {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "搜索内容")
    private String keyword;

}
