package com.sqx.app.waste.order.item.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.waste.order.item.domain.bo.WasteOrderItemInfoBo;
import com.sqx.app.waste.order.item.domain.bo.WasteOrderItemPageBo;
import com.sqx.app.waste.order.item.domain.bo.WasteOrderItemSaveBo;
import com.sqx.app.waste.order.item.domain.bo.WasteOrderItemUpdateBo;
import com.sqx.app.waste.order.item.domain.vo.WasteOrderItemVo;
import com.sqx.app.waste.order.item.service.WasteOrderItemService;
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
 * 订单关联的项目 前端控制器
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-03 14:28:51
 */
@Tag(name = "用户端-订单关联的项目", description = "用户端-订单关联的项目")
@RestController
@RequestMapping("/app/waste/order/item")
public class AppWasteOrderItemController {

    @Resource
    private WasteOrderItemService wasteOrderItemService;

    /**
     * 保存
     */
    @Operation(summary = "保存")
    @PostMapping("save")
    public Result<Void> save(@RequestBody @Validated WasteOrderItemSaveBo wasteOrderItemSaveBo) {
        wasteOrderItemService.saveInfo(wasteOrderItemSaveBo);
        return Result.success();
    }

    /**
     * 列表
     */
    @Operation(summary = "列表")
    @ApiResponse(content = @Content(schema = @Schema(implementation = WasteOrderItemVo.class)))
    @PostMapping("page")
    public Result<IPage<WasteOrderItemVo>> page(@RequestBody @Validated WasteOrderItemPageBo wasteOrderItemPageBo) {
        IPage<WasteOrderItemVo> voIPage = wasteOrderItemService.pageInfo(wasteOrderItemPageBo);
        return Result.success(voIPage);
    }

    /**
     * 信息
     */
    @Operation(summary = "信息")
    @ApiResponse(content = @Content(schema = @Schema(implementation = WasteOrderItemVo.class)))
    @PostMapping("info")
    public Result<WasteOrderItemVo> info(@RequestBody @Validated WasteOrderItemInfoBo wasteOrderItemInfoBo) {
        WasteOrderItemVo vo = wasteOrderItemService.info(wasteOrderItemInfoBo);
        return Result.success(vo);
    }

    /**
     * 更新
     */
    @Operation(summary = "更新")
    @PostMapping("update")
    public Result<Void> update(@RequestBody @Validated WasteOrderItemUpdateBo wasteOrderItemUpdateBo) {
        wasteOrderItemService.updateInfo(wasteOrderItemUpdateBo);
        return Result.success();
    }

    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("delete")
    public Result<Void> delete(@RequestBody @Validated WasteOrderItemInfoBo wasteOrderItemInfoBo) {
        wasteOrderItemService.delete(wasteOrderItemInfoBo);
        return Result.success();
    }
}
