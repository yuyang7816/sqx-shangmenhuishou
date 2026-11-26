package com.sqx.app.searchHistory.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.searchHistory.domain.bo.SearchBo;
import com.sqx.app.searchHistory.domain.bo.SearchHistoryInfoBo;
import com.sqx.app.searchHistory.domain.bo.SearchHistoryListBo;
import com.sqx.app.searchHistory.domain.bo.SearchHistoryPageBo;
import com.sqx.app.searchHistory.domain.vo.SearchHistoryVo;
import com.sqx.app.searchHistory.domain.vo.SearchVo;
import com.sqx.app.searchHistory.service.SearchHistoryService;
import com.sqx.common.core.domain.Result;
import com.sqx.common.shiro.app.annotation.Login;
import com.sqx.common.shiro.utils.UserContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户搜索历史 前端控制器
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-17 13:23:01
 */
@Tag(name = "用户端-用户搜索历史", description = "用户端-用户搜索历史")
@Validated
@RestController
@RequestMapping("/app/searchHistory")
public class AppSearchHistoryController {

    @Resource
    private SearchHistoryService searchHistoryService;

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

    @Operation(summary = "列表（不分页）")
    @ApiResponse(content = @Content(schema = @Schema(implementation = SearchHistoryVo.class)))
    @PostMapping("list")
    public Result<List<SearchHistoryVo>> list(@RequestBody @Validated SearchHistoryListBo searchHistoryListBo) {
        List<SearchHistoryVo> voList = searchHistoryService.listInfo(searchHistoryListBo);
        return Result.success(voList);
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

    @Login
    @Operation(summary = "清空搜索记录")
    @GetMapping("clear")
    public Result<Void> clear() {
        searchHistoryService.clear();
        return Result.success();
    }

    @Login
    @Operation(summary = "搜索")
    @PostMapping("search")
    public Result<SearchVo> search(@RequestBody SearchBo searchBo) {
        searchBo.setUserId(UserContextHolder.getAppUserId());
        SearchVo vo = searchHistoryService.search(searchBo);
        return Result.success(vo);
    }

    @Operation(summary = "热门搜索历史")
    @GetMapping("hot")
    public Result<List<SearchHistoryVo>> hot() {
        List<SearchHistoryVo> voList = searchHistoryService.hot();
        return Result.success(voList);
    }
}
