package com.sqx.app.base.app.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.base.app.domain.vo.AppVo;
import com.sqx.app.base.app.domain.bo.AppInfoBo;
import com.sqx.app.base.app.domain.bo.AppPageBo;
import com.sqx.app.base.app.domain.bo.AppSaveBo;
import com.sqx.app.base.app.domain.bo.AppUpdateBo;
import com.sqx.app.base.app.service.AppService;
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
 * app升级 前端控制器
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-11-18 15:55:50
 */
@Tag(name = "管理端-app升级", description = "管理端-app升级")
@RestController
@RequestMapping("/admin/app")
public class AdminAppController {

    @Resource
    private AppService appService;

    /**
     * 保存
     */
    @Operation(summary = "保存")
    @PostMapping("save")
    public Result<Void> save(@RequestBody @Validated AppSaveBo appSaveBo) {
        appService.saveInfo(appSaveBo);
        return Result.success();
    }

    /**
     * 列表
     */
    @Operation(summary = "列表")
    @ApiResponse(content = @Content(schema = @Schema(implementation = AppVo.class)))
    @PostMapping("page")
    public Result<IPage<AppVo>> page(@RequestBody @Validated AppPageBo appPageBo) {
        IPage<AppVo> voIPage = appService.pageInfo(appPageBo);
        return Result.success(voIPage);
    }

    /**
     * 信息
     */
    @Operation(summary = "信息")
    @ApiResponse(content = @Content(schema = @Schema(implementation = AppVo.class)))
    @PostMapping("info")
    public Result<AppVo> info(@RequestBody @Validated AppInfoBo appInfoBo) {
        AppVo vo = appService.info(appInfoBo);
        return Result.success(vo);
    }

    /**
     * 更新
     */
    @Operation(summary = "更新")
    @PostMapping("update")
    public Result<Void> update(@RequestBody @Validated AppUpdateBo appUpdateBo) {
        appService.updateInfo(appUpdateBo);
        return Result.success();
    }

    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("delete")
    public Result<Void> delete(@RequestBody @Validated AppInfoBo appInfoBo) {
        appService.delete(appInfoBo);
        return Result.success();
    }
}
