package com.sqx.app.waste.category.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.waste.category.domain.bo.*;
import com.sqx.app.waste.category.domain.vo.WasteCategoryVo;
import com.sqx.app.waste.category.service.WasteCategoryService;
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
import java.util.List;

/**
 * 废品类别 前端控制器
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-03 11:36:52
 */
@Tag(name = "用户端-废品类别", description = "用户端-废品类别")
@RestController
@RequestMapping("/app/waste/category")
public class AppWasteCategoryController {

    @Resource
    private WasteCategoryService wasteCategoryService;

    /**
     * 保存
     */
    @Operation(summary = "保存")
    @PostMapping("save")
    public Result<Void> save(@RequestBody @Validated WasteCategorySaveBo wasteCategorySaveBo) {
        wasteCategoryService.saveInfo(wasteCategorySaveBo);
        return Result.success();
    }

    /**
     * 列表
     */
    @Operation(summary = "列表")
    @ApiResponse(content = @Content(schema = @Schema(implementation = WasteCategoryVo.class)))
    @PostMapping("page")
    public Result<IPage<WasteCategoryVo>> page(@RequestBody @Validated WasteCategoryPageBo wasteCategoryPageBo) {
        IPage<WasteCategoryVo> voIPage = wasteCategoryService.pageInfo(wasteCategoryPageBo);
        return Result.success(voIPage);
    }

    /**
     * 信息
     */
    @Operation(summary = "信息")
    @ApiResponse(content = @Content(schema = @Schema(implementation = WasteCategoryVo.class)))
    @PostMapping("info")
    public Result<WasteCategoryVo> info(@RequestBody @Validated WasteCategoryInfoBo wasteCategoryInfoBo) {
        WasteCategoryVo vo = wasteCategoryService.info(wasteCategoryInfoBo);
        return Result.success(vo);
    }

    /**
     * 更新
     */
    @Operation(summary = "更新")
    @PostMapping("update")
    public Result<Void> update(@RequestBody @Validated WasteCategoryUpdateBo wasteCategoryUpdateBo) {
        wasteCategoryService.updateInfo(wasteCategoryUpdateBo);
        return Result.success();
    }

    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("delete")
    public Result<Void> delete(@RequestBody @Validated WasteCategoryInfoBo wasteCategoryInfoBo) {
        wasteCategoryService.delete(wasteCategoryInfoBo);
        return Result.success();
    }

    @Operation(summary = "列表(不分页)")
    @ApiResponse(content = @Content(schema = @Schema(implementation = WasteCategoryVo.class)))
    @PostMapping("list")
    public Result<List<WasteCategoryVo>> list(@RequestBody @Validated WasteCategoryListBo wasteCategoryListBo) {
        List<WasteCategoryVo> voList = wasteCategoryService.listInfo(wasteCategoryListBo);
        return Result.success(voList);
    }
}
