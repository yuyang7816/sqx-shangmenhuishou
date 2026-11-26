package com.sqx.app.payment.record.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.payment.record.domain.bo.UserPaymentRecordInfoBo;
import com.sqx.app.payment.record.domain.bo.UserPaymentRecordPageBo;
import com.sqx.app.payment.record.domain.bo.UserPaymentRecordSaveBo;
import com.sqx.app.payment.record.domain.bo.UserPaymentRecordUpdateBo;
import com.sqx.app.payment.record.domain.vo.UserPaymentRecordVo;
import com.sqx.app.payment.record.service.UserPaymentRecordService;
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
 * 用户支付记录表 前端控制器
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-08-13 15:19:30
 */
@Tag(name = "用户端-用户支付记录表", description = "用户端-用户支付记录表")
@RestController
@RequestMapping("/app/userPaymentRecord")
public class AppUserPaymentRecordController {

    @Resource
    private UserPaymentRecordService userPaymentRecordService;

    /**
     * 保存
     */
    @Operation(summary = "保存")
    @PostMapping("save")
    public Result<Void> save(@RequestBody @Validated UserPaymentRecordSaveBo userPaymentRecordSaveBo) {
        userPaymentRecordService.saveInfo(userPaymentRecordSaveBo);
        return Result.success();
    }

    /**
     * 列表
     */
    @Operation(summary = "列表")
    @ApiResponse(content = @Content(schema = @Schema(implementation = UserPaymentRecordVo.class)))
    @PostMapping("page")
    public Result<IPage<UserPaymentRecordVo>> page(@RequestBody @Validated UserPaymentRecordPageBo userPaymentRecordPageBo) {
        IPage<UserPaymentRecordVo> voIPage = userPaymentRecordService.pageInfo(userPaymentRecordPageBo);
        return Result.success(voIPage);
    }

    /**
     * 信息
     */
    @Operation(summary = "信息")
    @ApiResponse(content = @Content(schema = @Schema(implementation = UserPaymentRecordVo.class)))
    @PostMapping("info")
    public Result<UserPaymentRecordVo> info(@RequestBody @Validated UserPaymentRecordInfoBo userPaymentRecordInfoBo) {
        UserPaymentRecordVo vo = userPaymentRecordService.info(userPaymentRecordInfoBo);
        return Result.success(vo);
    }

    /**
     * 更新
     */
    @Operation(summary = "更新")
    @PostMapping("update")
    public Result<Void> update(@RequestBody @Validated UserPaymentRecordUpdateBo userPaymentRecordUpdateBo) {
        userPaymentRecordService.updateInfo(userPaymentRecordUpdateBo);
        return Result.success();
    }

    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("delete")
    public Result<Void> delete(@RequestBody @Validated UserPaymentRecordInfoBo userPaymentRecordInfoBo) {
        userPaymentRecordService.delete(userPaymentRecordInfoBo);
        return Result.success();
    }
}
