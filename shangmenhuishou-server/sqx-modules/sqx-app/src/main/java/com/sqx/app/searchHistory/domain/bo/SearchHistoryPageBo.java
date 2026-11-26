package com.sqx.app.searchHistory.domain.bo;

import com.sqx.common.mybatis.domain.PageParam ;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-17 13:23:01
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SearchHistoryPageBo extends PageParam {
    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "关键字")
    private String keyword;
}
