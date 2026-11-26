package com.sqx.app.deposit.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.deposit.domain.bo.DepositRecordPageBo;
import com.sqx.app.deposit.domain.vo.DepositRecordVo;
import com.sqx.app.deposit.service.DepositRecordService;
import com.sqx.common.core.domain.Result;
import com.sqx.common.shiro.app.annotation.Login;
import com.sqx.common.shiro.utils.UserContextHolder;
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
 * 保证金记录 前端控制器
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-10-13 14:47:55
 */
@Tag(name = "用户端-保证金记录", description = "用户端-保证金记录")
@RestController
@RequestMapping("/app/depositRecord")
public class AppDepositRecordController {

    @Resource
    private DepositRecordService depositRecordService;

    /**
     * 列表
     */
    @Login
    @Operation(summary = "列表")
    @ApiResponse(content = @Content(schema = @Schema(implementation = DepositRecordVo.class)))
    @PostMapping("page")
    public Result<IPage<DepositRecordVo>> page(@RequestBody @Validated DepositRecordPageBo depositRecordPageBo) {
        depositRecordPageBo.setUserId(UserContextHolder.getAppUserId());
        IPage<DepositRecordVo> voIPage = depositRecordService.pageInfo(depositRecordPageBo);
        return Result.success(voIPage);
    }
}
