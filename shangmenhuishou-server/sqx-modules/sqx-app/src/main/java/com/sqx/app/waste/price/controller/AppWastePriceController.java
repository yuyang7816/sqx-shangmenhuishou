package com.sqx.app.waste.price.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.waste.price.domain.bo.*;
import com.sqx.app.waste.price.domain.vo.WastePriceVo;
import com.sqx.app.waste.price.service.WastePriceService;
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
 * 废品价格 前端控制器
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-03 11:37:16
 */
@Tag(name = "用户端-废品价格", description = "用户端-废品价格")
@RestController
@RequestMapping("/app/waste/price")
public class AppWastePriceController {

    @Resource
    private WastePriceService wastePriceService;

    /**
     * 保存
     */
    @Operation(summary = "保存")
    @PostMapping("save")
    public Result<Void> save(@RequestBody @Validated WastePriceSaveBo wastePriceSaveBo) {
        wastePriceService.saveInfo(wastePriceSaveBo);
        return Result.success();
    }

    /**
     * 列表
     */
    @Operation(summary = "列表")
    @ApiResponse(content = @Content(schema = @Schema(implementation = WastePriceVo.class)))
    @PostMapping("page")
    public Result<IPage<WastePriceVo>> page(@RequestBody @Validated WastePricePageBo wastePricePageBo) {
        IPage<WastePriceVo> voIPage = wastePriceService.pageInfo(wastePricePageBo);
        return Result.success(voIPage);
    }

    /**
     * 信息
     */
    @Operation(summary = "信息")
    @ApiResponse(content = @Content(schema = @Schema(implementation = WastePriceVo.class)))
    @PostMapping("info")
    public Result<WastePriceVo> info(@RequestBody @Validated WastePriceInfoBo wastePriceInfoBo) {
        WastePriceVo vo = wastePriceService.info(wastePriceInfoBo);
        return Result.success(vo);
    }

    /**
     * 更新
     */
    @Operation(summary = "更新")
    @PostMapping("update")
    public Result<Void> update(@RequestBody @Validated WastePriceUpdateBo wastePriceUpdateBo) {
        wastePriceService.updateInfo(wastePriceUpdateBo);
        return Result.success();
    }

    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("delete")
    public Result<Void> delete(@RequestBody @Validated WastePriceInfoBo wastePriceInfoBo) {
        wastePriceService.delete(wastePriceInfoBo);
        return Result.success();
    }

    @Operation(summary = "列表（不分页）")
    @ApiResponse(content = @Content(schema = @Schema(implementation = WastePriceVo.class)))
    @PostMapping("list")
    public Result<List<WastePriceVo>> list(@RequestBody @Validated WastePriceListBo wastePriceListBo) {
        List<WastePriceVo> voList = wastePriceService.listInfo(wastePriceListBo);
        return Result.success(voList);
    }

}
