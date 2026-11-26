package com.sqx.app.waste.item.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.waste.item.domain.bo.WasteItemInfoBo;
import com.sqx.app.waste.item.domain.bo.WasteItemPageBo;
import com.sqx.app.waste.item.domain.bo.WasteItemPageByCategoryIdBo;
import com.sqx.app.waste.item.domain.vo.WasteItemVo;
import com.sqx.app.waste.item.service.WasteItemService;
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
 * 回收项目 前端控制器
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-28 15:47:04
 */
@Tag(name = "用户端-回收项目", description = "用户端-回收项目")
@RestController
@RequestMapping("/app/wasteItem")
public class AppWasteItemController {

    @Resource
    private WasteItemService wasteItemService;

    /**
     * 列表
     */
    @Operation(summary = "列表")
    @ApiResponse(content = @Content(schema = @Schema(implementation = WasteItemVo.class)))
    @PostMapping("page")
    public Result<IPage<WasteItemVo>> page(@RequestBody @Validated WasteItemPageBo wasteItemPageBo) {
        IPage<WasteItemVo> voIPage = wasteItemService.pageInfo(wasteItemPageBo);
        return Result.success(voIPage);
    }

    /**
     * 信息
     */
    @Operation(summary = "信息")
    @ApiResponse(content = @Content(schema = @Schema(implementation = WasteItemVo.class)))
    @PostMapping("info")
    public Result<WasteItemVo> info(@RequestBody @Validated WasteItemInfoBo wasteItemInfoBo) {
        WasteItemVo vo = wasteItemService.info(wasteItemInfoBo);
        return Result.success(vo);
    }

    @Operation(summary = "分类列表", description = "根据父类型查询回收项目")
    @ApiResponse(content = @Content(schema = @Schema(implementation = WasteItemVo.class)))
    @PostMapping("pageByCategoryId")
    public Result<IPage<WasteItemVo>> pageByCategoryId(@RequestBody @Validated WasteItemPageByCategoryIdBo wasteItemPageByCategoryIdBo) {
        IPage<WasteItemVo> voIPage = wasteItemService.pageByCategoryId(wasteItemPageByCategoryIdBo);
        return Result.success(voIPage);
    }
}
