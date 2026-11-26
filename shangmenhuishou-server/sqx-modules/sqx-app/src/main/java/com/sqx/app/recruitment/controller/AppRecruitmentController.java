package com.sqx.app.recruitment.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.recruitment.domain.bo.*;
import com.sqx.app.recruitment.domain.vo.RecruitmentVo;
import com.sqx.app.recruitment.service.RecruitmentService;
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

/**
 * 招募 前端控制器
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-17 15:30:33
 */
@Tag(name = "用户端-招募", description = "用户端-招募")
@RestController
@RequestMapping("/app/recruitment")
public class AppRecruitmentController {

    @Resource
    private RecruitmentService recruitmentService;

    /**
     * 保存
     */
    @Login
    @Operation(summary = "保存")
    @PostMapping("save")
    public Result<Void> save(@RequestBody @Validated RecruitmentSaveBo recruitmentSaveBo) {
        recruitmentSaveBo.setUserId(UserContextHolder.getAppUserId());
        recruitmentService.saveInfo(recruitmentSaveBo);
        return Result.success();
    }

    /**
     * 列表
     */
    @Operation(summary = "列表")
    @ApiResponse(content = @Content(schema = @Schema(implementation = RecruitmentVo.class)))
    @PostMapping("page")
    public Result<IPage<RecruitmentVo>> page(@RequestBody @Validated RecruitmentPageBo recruitmentPageBo) {
        IPage<RecruitmentVo> voIPage = recruitmentService.pageInfo(recruitmentPageBo);
        return Result.success(voIPage);
    }

    /**
     * 信息
     */
    @Operation(summary = "信息")
    @ApiResponse(content = @Content(schema = @Schema(implementation = RecruitmentVo.class)))
    @PostMapping("info")
    public Result<RecruitmentVo> info(@RequestBody @Validated RecruitmentInfoBo recruitmentInfoBo) {
        RecruitmentVo vo = recruitmentService.info(recruitmentInfoBo);
        return Result.success(vo);
    }

    /**
     * 更新
     */
    @Operation(summary = "更新")
    @PostMapping("update")
    public Result<Void> update(@RequestBody @Validated RecruitmentUpdateBo recruitmentUpdateBo) {
        recruitmentService.updateInfo(recruitmentUpdateBo);
        return Result.success();
    }

    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("delete")
    public Result<Void> delete(@RequestBody @Validated RecruitmentInfoBo recruitmentInfoBo) {
        recruitmentService.delete(recruitmentInfoBo);
        return Result.success();
    }

    @Operation(summary = "审核")
    @PostMapping("audit")
    public Result<Void> audit(@RequestBody @Validated RecruitmentAuditBo recruitmentAuditBo) {
        recruitmentService.audit(recruitmentAuditBo);
        return Result.success();
    }

    @Login
    @Operation(summary = "我的招募信息")
    @ApiResponse(content = @Content(schema = @Schema(implementation = RecruitmentVo.class)))
    @GetMapping("myInfo")
    public Result<RecruitmentVo> myInfo() {
        RecruitmentVo vo = recruitmentService.myInfo();
        return Result.success(vo);
    }
}
