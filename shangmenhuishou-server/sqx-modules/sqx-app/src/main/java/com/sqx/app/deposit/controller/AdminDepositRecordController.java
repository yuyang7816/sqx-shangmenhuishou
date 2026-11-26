package com.sqx.app.deposit.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.deposit.domain.vo.DepositRecordVo;
import com.sqx.app.deposit.domain.bo.DepositRecordInfoBo;
import com.sqx.app.deposit.domain.bo.DepositRecordPageBo;
import com.sqx.app.deposit.domain.bo.DepositRecordSaveBo;
import com.sqx.app.deposit.domain.bo.DepositRecordUpdateBo;
import com.sqx.app.deposit.service.DepositRecordService;
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
 * 保证金记录 前端控制器
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-10-13 14:47:55
 */
@Tag(name = "管理端-保证金记录", description = "管理端-保证金记录")
@RestController
@RequestMapping("/admin/depositRecord")
public class AdminDepositRecordController {

    @Resource
    private DepositRecordService depositRecordService;

    /**
     * 保存
     */
    @Operation(summary = "保存")
    @PostMapping("save")
    public Result<Void> save(@RequestBody @Validated DepositRecordSaveBo depositRecordSaveBo) {
        depositRecordService.saveInfo(depositRecordSaveBo);
        return Result.success();
    }

    /**
     * 列表
     */
    @Operation(summary = "列表")
    @ApiResponse(content = @Content(schema = @Schema(implementation = DepositRecordVo.class)))
    @PostMapping("page")
    public Result<IPage<DepositRecordVo>> page(@RequestBody @Validated DepositRecordPageBo depositRecordPageBo) {
        IPage<DepositRecordVo> voIPage = depositRecordService.pageInfo(depositRecordPageBo);
        return Result.success(voIPage);
    }

    /**
     * 信息
     */
    @Operation(summary = "信息")
    @ApiResponse(content = @Content(schema = @Schema(implementation = DepositRecordVo.class)))
    @PostMapping("info")
    public Result<DepositRecordVo> info(@RequestBody @Validated DepositRecordInfoBo depositRecordInfoBo) {
        DepositRecordVo vo = depositRecordService.info(depositRecordInfoBo);
        return Result.success(vo);
    }

    /**
     * 更新
     */
    @Operation(summary = "更新")
    @PostMapping("update")
    public Result<Void> update(@RequestBody @Validated DepositRecordUpdateBo depositRecordUpdateBo) {
        depositRecordService.updateInfo(depositRecordUpdateBo);
        return Result.success();
    }

    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("delete")
    public Result<Void> delete(@RequestBody @Validated DepositRecordInfoBo depositRecordInfoBo) {
        depositRecordService.delete(depositRecordInfoBo);
        return Result.success();
    }
}
