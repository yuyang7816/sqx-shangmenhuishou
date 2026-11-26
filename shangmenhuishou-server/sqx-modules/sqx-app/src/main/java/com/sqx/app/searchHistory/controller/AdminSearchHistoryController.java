package com.sqx.app.searchHistory.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.searchHistory.domain.vo.SearchHistoryVo;
import com.sqx.app.searchHistory.domain.bo.SearchHistoryInfoBo;
import com.sqx.app.searchHistory.domain.bo.SearchHistoryPageBo;
import com.sqx.app.searchHistory.domain.bo.SearchHistorySaveBo;
import com.sqx.app.searchHistory.domain.bo.SearchHistoryUpdateBo;
import com.sqx.app.searchHistory.service.SearchHistoryService;
import com.sqx.common.core.domain.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

/**
 * 用户搜索历史 前端控制器
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-17 13:23:01
 */
@Tag(name = "管理端-用户搜索历史", description = "管理端-用户搜索历史")
@RestController
@RequestMapping("/admin/searchHistory")
public class AdminSearchHistoryController {

    @Resource
    private SearchHistoryService searchHistoryService;

    /**
     * 保存
     */
    @Operation(summary = "保存")
    @PostMapping("save")
    public Result<Void> save(@RequestBody @Validated SearchHistorySaveBo searchHistorySaveBo) {
        searchHistoryService.saveInfo(searchHistorySaveBo);
        return Result.success();
    }

    /**
     * 列表
     */
    @Operation(summary = "列表")
    @ApiResponse(content = @Content(schema = @Schema(implementation = SearchHistoryVo.class)))
    @PostMapping("page")
    public Result<IPage<SearchHistoryVo>> page(@RequestBody @Validated SearchHistoryPageBo searchHistoryPageBo) {
        IPage<SearchHistoryVo> voIPage = searchHistoryService.pageInfo(searchHistoryPageBo);
        return Result.success(voIPage);
    }

    /**
     * 信息
     */
    @Operation(summary = "信息")
    @ApiResponse(content = @Content(schema = @Schema(implementation = SearchHistoryVo.class)))
    @PostMapping("info")
    public Result<SearchHistoryVo> info(@RequestBody @Validated SearchHistoryInfoBo searchHistoryInfoBo) {
        SearchHistoryVo vo = searchHistoryService.info(searchHistoryInfoBo);
        return Result.success(vo);
    }

    /**
     * 更新
     */
    @Operation(summary = "更新")
    @PostMapping("update")
    public Result<Void> update(@RequestBody @Validated SearchHistoryUpdateBo searchHistoryUpdateBo) {
        searchHistoryService.updateInfo(searchHistoryUpdateBo);
        return Result.success();
    }

    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("delete")
    public Result<Void> delete(@RequestBody @Validated SearchHistoryInfoBo searchHistoryInfoBo) {
        searchHistoryService.delete(searchHistoryInfoBo);
        return Result.success();
    }
}
