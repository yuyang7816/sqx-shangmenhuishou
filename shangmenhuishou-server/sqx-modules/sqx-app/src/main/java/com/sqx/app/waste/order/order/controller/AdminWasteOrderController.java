package com.sqx.app.waste.order.order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.waste.order.order.domain.vo.WasteOrderVo;
import com.sqx.app.waste.order.order.domain.bo.WasteOrderInfoBo;
import com.sqx.app.waste.order.order.domain.bo.WasteOrderPageBo;
import com.sqx.app.waste.order.order.domain.bo.WasteOrderSaveBo;
import com.sqx.app.waste.order.order.domain.bo.WasteOrderUpdateBo;
import com.sqx.app.waste.order.order.service.WasteOrderService;
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
 * 订单 前端控制器
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-03 14:28:34
 */
@Tag(name = "管理端-订单", description = "管理端-订单")
@RestController
@RequestMapping("/admin/waste/order")
public class AdminWasteOrderController {

    @Resource
    private WasteOrderService wasteOrderService;

    /**
     * 保存
     */
    @Operation(summary = "保存")
    @PostMapping("save")
    public Result<Void> save(@RequestBody @Validated WasteOrderSaveBo wasteOrderSaveBo) {
        wasteOrderService.saveInfo(wasteOrderSaveBo);
        return Result.success();
    }

    /**
     * 列表
     */
    @Operation(summary = "列表")
    @ApiResponse(content = @Content(schema = @Schema(implementation = WasteOrderVo.class)))
    @PostMapping("page")
    public Result<IPage<WasteOrderVo>> page(@RequestBody @Validated WasteOrderPageBo wasteOrderPageBo) {
        IPage<WasteOrderVo> voIPage = wasteOrderService.pageInfo(wasteOrderPageBo);
        return Result.success(voIPage);
    }

    /**
     * 信息
     */
    @Operation(summary = "信息")
    @ApiResponse(content = @Content(schema = @Schema(implementation = WasteOrderVo.class)))
    @PostMapping("info")
    public Result<WasteOrderVo> info(@RequestBody @Validated WasteOrderInfoBo wasteOrderInfoBo) {
        WasteOrderVo vo = wasteOrderService.info(wasteOrderInfoBo);
        return Result.success(vo);
    }

    /**
     * 更新
     */
    @Operation(summary = "更新")
    @PostMapping("update")
    public Result<Void> update(@RequestBody @Validated WasteOrderUpdateBo wasteOrderUpdateBo) {
        wasteOrderService.updateInfo(wasteOrderUpdateBo);
        return Result.success();
    }

    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("delete")
    public Result<Void> delete(@RequestBody @Validated WasteOrderInfoBo wasteOrderInfoBo) {
        wasteOrderService.delete(wasteOrderInfoBo);
        return Result.success();
    }
}
