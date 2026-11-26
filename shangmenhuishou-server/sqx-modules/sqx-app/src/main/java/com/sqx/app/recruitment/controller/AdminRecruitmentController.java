package com.sqx.app.recruitment.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.recruitment.domain.vo.RecruitmentVo;
import com.sqx.app.recruitment.domain.bo.RecruitmentInfoBo;
import com.sqx.app.recruitment.domain.bo.RecruitmentPageBo;
import com.sqx.app.recruitment.domain.bo.RecruitmentSaveBo;
import com.sqx.app.recruitment.domain.bo.RecruitmentUpdateBo;
import com.sqx.app.recruitment.service.RecruitmentService;
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
 * 招募 前端控制器
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-17 15:30:33
 */
@Tag(name = "管理端-招募", description = "管理端-招募")
@RestController
@RequestMapping("/admin/recruitment")
public class AdminRecruitmentController {

    @Resource
    private RecruitmentService recruitmentService;

    /**
     * 保存
     */
    @Operation(summary = "保存")
    @PostMapping("save")
    public Result<Void> save(@RequestBody @Validated RecruitmentSaveBo recruitmentSaveBo) {
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
}
