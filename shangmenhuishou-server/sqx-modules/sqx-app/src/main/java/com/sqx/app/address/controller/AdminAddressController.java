package com.sqx.app.address.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.address.domain.bo.*;
import com.sqx.app.address.domain.vo.AddressVo;
import com.sqx.app.address.service.AddressService;
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
 * 地址 前端控制器
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-17 13:54:22
 */
@Tag(name = "管理端-地址", description = "管理端-地址")
@RestController
@RequestMapping("/admin/address")
public class AdminAddressController {

    @Resource
    private AddressService addressService;

    /**
     * 保存
     */
    @Operation(summary = "保存")
    @PostMapping("save")
    public Result<Void> save(@RequestBody @Validated AddressSaveBo addressSaveBo) {
        addressService.saveInfo(addressSaveBo);
        return Result.success();
    }

    /**
     * 列表
     */
    @Operation(summary = "列表")
    @ApiResponse(content = @Content(schema = @Schema(implementation = AddressVo.class)))
    @PostMapping("page")
    public Result<IPage<AddressVo>> page(@RequestBody @Validated AddressPageBo addressPageBo) {
        IPage<AddressVo> voIPage = addressService.pageInfo(addressPageBo);
        return Result.success(voIPage);
    }

    /**
     * 信息
     */
    @Operation(summary = "信息")
    @ApiResponse(content = @Content(schema = @Schema(implementation = AddressVo.class)))
    @PostMapping("info")
    public Result<AddressVo> info(@RequestBody @Validated AddressInfoBo addressInfoBo) {
        AddressVo vo = addressService.info(addressInfoBo);
        return Result.success(vo);
    }

    /**
     * 更新
     */
    @Operation(summary = "更新")
    @PostMapping("update")
    public Result<Void> update(@RequestBody @Validated AddressUpdateBo addressUpdateBo) {
        addressService.updateInfo(addressUpdateBo);
        return Result.success();
    }

    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("delete")
    public Result<Void> delete(@RequestBody @Validated AddressInfoBo addressInfoBo) {
        addressService.delete(addressInfoBo);
        return Result.success();
    }

    @Operation(summary = "修改默认状态")
    @PostMapping("updateDefault")
    public Result<Void> updateDefault(@RequestBody @Validated AddressUpdateDefaultBo addressUpdateDefaultBo) {
        addressService.updateDefault(addressUpdateDefaultBo);
        return Result.success();
    }
}
