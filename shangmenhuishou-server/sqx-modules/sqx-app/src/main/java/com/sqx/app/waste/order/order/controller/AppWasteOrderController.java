package com.sqx.app.waste.order.order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.waste.order.order.domain.bo.*;
import com.sqx.app.waste.order.order.domain.vo.TakeOrderDataVo;
import com.sqx.app.waste.order.order.domain.vo.WasteOrderDataVo;
import com.sqx.app.waste.order.order.domain.vo.WasteOrderMyVo;
import com.sqx.app.waste.order.order.domain.vo.WasteOrderVo;
import com.sqx.app.waste.order.order.service.WasteOrderService;
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
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 订单 前端控制器
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-03 14:28:34
 */
@Tag(name = "用户端-订单", description = "用户端-订单")
@RestController
@RequestMapping("/app/waste/order")
public class AppWasteOrderController {

    @Resource
    private WasteOrderService wasteOrderService;

    /**
     * 保存
     */
    @Login
    @Operation(summary = "保存")
    @PostMapping("save")
    public Result<Void> save(@RequestBody @Validated WasteOrderSaveBo wasteOrderSaveBo) {
        Long userId = UserContextHolder.getAppUserId();
        wasteOrderSaveBo.setUserId(userId);
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

    @Login
    @Operation(summary = "接单")
    @PostMapping("take")
    public Result<Void> take(@RequestBody @Validated WasteOrderTakeBo wasteOrderTakeBo) {
        wasteOrderTakeBo.setUserId(UserContextHolder.getAppUserId());
        wasteOrderService.take(wasteOrderTakeBo);
        return Result.success();
    }

    @Login
    @Operation(summary = "用户取消订单")
    @PostMapping("cancel")
    public Result<Void> cancel(@RequestBody @Validated WasteOrderCancelBo wasteOrderCancelBo) {
        wasteOrderCancelBo.setUserId(UserContextHolder.getAppUserId());
        wasteOrderService.cancel(wasteOrderCancelBo);
        return Result.success();
    }

    @Login
    @Operation(summary = "回收员取消订单")
    @PostMapping("wasteCancel")
    public Result<Void> wasteCancel(@RequestBody @Validated WasteOrderCancelBo wasteOrderCancelBo) {
        wasteOrderCancelBo.setUserId(UserContextHolder.getAppUserId());
        wasteOrderService.wasteCancel(wasteOrderCancelBo);
        return Result.success();
    }

    @Login
    @Operation(summary = "开始服务订单")
    @PostMapping("start")
    public Result<Void> start(@RequestBody @Validated WasteOrderStartBo wasteOrderStartBo) {
        wasteOrderStartBo.setUserId(UserContextHolder.getAppUserId());
        wasteOrderService.start(wasteOrderStartBo);
        return Result.success();
    }

    @Login
    @Operation(summary = "停止服务订单（回收员）", description = "回收员在接单后取消服务，原订单回到接单池中")
    @PostMapping("stop")
    public Result<Void> stop(@RequestBody @Validated WasteOrderStopBo wasteOrderStopBo) {
        wasteOrderStopBo.setUserId(UserContextHolder.getAppUserId());
        wasteOrderService.stop(wasteOrderStopBo);
        return Result.success();
    }

    @Login
    @Operation(summary = "完成订单")
    @PostMapping("complete")
    public Result<Void> complete(@RequestBody @Validated WasteOrderCompleteBo wasteOrderCompleteBo) {
        wasteOrderCompleteBo.setUserId(UserContextHolder.getAppUserId());
        wasteOrderService.complete(wasteOrderCompleteBo);
        return Result.success();
    }

    @Login
    @Operation(summary = "我的订单(用户端)")
    @GetMapping("myOrder")
    public Result<List<WasteOrderMyVo>> myOrder() {
        List<WasteOrderMyVo> voList = wasteOrderService.myOrder();
        return Result.success(voList);
    }

    @Login
    @Operation(summary = "回收员接单统计")
    @GetMapping("takeOrderData")
    public Result<TakeOrderDataVo> takeOrderData() {
        TakeOrderDataVo vo = wasteOrderService.takeOrderData();
        return Result.success(vo);
    }

    @Login
    @Operation(summary = "回收员订单数据")
    @GetMapping("wasteOrderData")
    public Result<List<WasteOrderDataVo>> wasteOrderData() {
        List<WasteOrderDataVo> voList = wasteOrderService.wasteOrderData();
        return Result.success(voList);
    }

    @Login
    @Operation(summary = "接单池")
    @ApiResponse(content = @Content(schema = @Schema(implementation = WasteOrderVo.class)))
    @GetMapping("pool")
    public Result<IPage<WasteOrderVo>> pool(Long page, Long limit, @NotBlank(message = "请输入城市") String city) {
        IPage<WasteOrderVo> voIPage = wasteOrderService.pool(page, limit, city);
        return Result.success(voIPage);
    }

    @Operation(summary = "详细信息")
    @ApiResponse(content = @Content(schema = @Schema(implementation = WasteOrderVo.class)))
    @PostMapping("information")
    public Result<WasteOrderVo> information(@RequestBody @Validated WasteOrderInformationBo wasteOrderInformationBo) {
        WasteOrderVo vo = wasteOrderService.information(wasteOrderInformationBo);
        return Result.success(vo);
    }

    @Login
    @Operation(summary = "回收项目下单")
    @PostMapping("itemOrder")
    public Result<Void> itemOrder(@RequestBody @Validated ItemOrderBo itemOrderBo) {
        Long userId = UserContextHolder.getAppUserId();
        itemOrderBo.setUserId(userId);
        wasteOrderService.itemOrder(itemOrderBo);
        return Result.success();
    }
}
