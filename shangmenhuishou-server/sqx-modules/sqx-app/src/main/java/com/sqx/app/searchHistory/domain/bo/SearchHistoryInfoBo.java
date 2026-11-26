package com.sqx.app.searchHistory.domain.bo;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

/**
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-17 13:23:01
 */
@Data
public class SearchHistoryInfoBo {

    @Schema(description = "id")
    @NotNull(message = "id不能为空")
    private Long id;

}
