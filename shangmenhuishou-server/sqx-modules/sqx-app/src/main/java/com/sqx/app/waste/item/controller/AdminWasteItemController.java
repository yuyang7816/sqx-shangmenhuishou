package com.sqx.app.waste.item.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.waste.item.domain.vo.WasteItemVo;
import com.sqx.app.waste.item.domain.bo.WasteItemInfoBo;
import com.sqx.app.waste.item.domain.bo.WasteItemPageBo;
import com.sqx.app.waste.item.domain.bo.WasteItemSaveBo;
import com.sqx.app.waste.item.domain.bo.WasteItemUpdateBo;
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
@Tag(name = "管理端-回收项目", description = "管理端-回收项目")
@RestController
@RequestMapping("/admin/wasteItem")
public class AdminWasteItemController {

    @Resource
    private WasteItemService wasteItemService;

    /**
     * 保存
     */
    @Operation(summary = "保存")
    @PostMapping("save")
    public Result<Void> save(@RequestBody @Validated WasteItemSaveBo wasteItemSaveBo) {
        wasteItemService.saveInfo(wasteItemSaveBo);
        return Result.success();
    }

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

    /**
     * 更新
     */
    @Operation(summary = "更新")
    @PostMapping("update")
    public Result<Void> update(@RequestBody @Validated WasteItemUpdateBo wasteItemUpdateBo) {
        wasteItemService.updateInfo(wasteItemUpdateBo);
        return Result.success();
    }

    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("delete")
    public Result<Void> delete(@RequestBody @Validated WasteItemInfoBo wasteItemInfoBo) {
        wasteItemService.delete(wasteItemInfoBo);
        return Result.success();
    }
}
