package com.sqx.app.authentication.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.authentication.domain.bo.*;
import com.sqx.app.authentication.domain.vo.UserAuthenticationVo;
import com.sqx.app.authentication.service.UserAuthenticationService;
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
 * 用户实名认证 前端控制器
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-01 14:08:36
 */
@Tag(name = "管理端-用户实名认证", description = "管理端-用户实名认证")
@RestController
@RequestMapping("/admin/userAuthentication")
public class AdminUserAuthenticationController {

    @Resource
    private UserAuthenticationService userAuthenticationService;

    /**
     * 保存
     */
    @Operation(summary = "保存")
    @PostMapping("save")
    public Result<Void> save(@RequestBody @Validated UserAuthenticationSaveBo userAuthenticationSaveBo) {
        userAuthenticationService.saveInfo(userAuthenticationSaveBo);
        return Result.success();
    }

    /**
     * 列表
     */
    @Operation(summary = "列表")
    @ApiResponse(content = @Content(schema = @Schema(implementation = UserAuthenticationVo.class)))
    @PostMapping("page")
    public Result<IPage<UserAuthenticationVo>> page(@RequestBody @Validated UserAuthenticationPageBo userAuthenticationPageBo) {
        IPage<UserAuthenticationVo> voIPage = userAuthenticationService.pageInfo(userAuthenticationPageBo);
        return Result.success(voIPage);
    }

    /**
     * 信息
     */
    @Operation(summary = "信息")
    @ApiResponse(content = @Content(schema = @Schema(implementation = UserAuthenticationVo.class)))
    @PostMapping("info")
    public Result<UserAuthenticationVo> info(@RequestBody @Validated UserAuthenticationInfoBo userAuthenticationInfoBo) {
        UserAuthenticationVo vo = userAuthenticationService.info(userAuthenticationInfoBo);
        return Result.success(vo);
    }

    /**
     * 更新
     */
    @Operation(summary = "更新")
    @PostMapping("update")
    public Result<Void> update(@RequestBody @Validated UserAuthenticationUpdateBo userAuthenticationUpdateBo) {
        userAuthenticationService.updateInfo(userAuthenticationUpdateBo);
        return Result.success();
    }

    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("delete")
    public Result<Void> delete(@RequestBody @Validated UserAuthenticationInfoBo userAuthenticationInfoBo) {
        userAuthenticationService.delete(userAuthenticationInfoBo);
        return Result.success();
    }

    @Operation(summary = "审核")
    @PostMapping("audit")
    public Result<Void> audit(@RequestBody @Validated UserAuthenticationAuditBo userAuthenticationAuditBo) {
        userAuthenticationService.audit(userAuthenticationAuditBo);
        return Result.success();
    }
}
