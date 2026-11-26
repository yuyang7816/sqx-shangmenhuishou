package com.sqx.app.searchHistory.domain.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.waste.item.domain.vo.WasteItemVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author chengming
 * @since 2025/9/17
 */
@Data
public class SearchVo {

    @Schema(description = "回收项目列表")
    private IPage<WasteItemVo> wasteItems;
}
