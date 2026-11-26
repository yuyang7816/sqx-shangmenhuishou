package com.sqx.app.index.controller;

import com.sqx.app.index.domain.bo.StatisticsBo;
import com.sqx.app.index.domain.vo.IndexStatisticsVo;
import com.sqx.app.index.domain.vo.OrderStatisticsVo;
import com.sqx.app.index.domain.vo.UserStatisticsVo;
import com.sqx.app.index.service.IndexService;
import com.sqx.app.referral.withdraw.domain.vo.WithdrawStatisticsVo;
import com.sqx.common.core.domain.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author chengming
 * @since 2025/10/14
 */
@Tag(name = "管理端-首页", description = "管理端-首页")
@RestController
@RequestMapping("admin/index")
@Validated
public class AdminIndexController {

    @Resource
    private IndexService indexService;

    @Operation(summary = "首页统计")
    @GetMapping("indexStatistics")
    public Result<IndexStatisticsVo> indexStatistics() {
        IndexStatisticsVo vo = indexService.indexStatistics();
        return Result.success(vo);
    }

    @Operation(summary = "用户分析")
    @PostMapping("userStatistics")
    public Result<UserStatisticsVo> userStatistics(@RequestBody @Validated StatisticsBo statisticsBo) {
        UserStatisticsVo vo = indexService.userStatistics(statisticsBo);
        return Result.success(vo);
    }

    @Operation(summary = "订单统计")
    @PostMapping("orderStatistics")
    public Result<OrderStatisticsVo> orderStatistics(@RequestBody StatisticsBo statisticsBo) {
        OrderStatisticsVo vo = indexService.orderStatistics(statisticsBo);
        return Result.success(vo);
    }

    @Operation(summary = "提现统计")
    @PostMapping("withdrawStatistics")
    public Result<WithdrawStatisticsVo> statistics(@RequestBody StatisticsBo statisticsBo) {
        WithdrawStatisticsVo vo = indexService.withdrawStatistics(statisticsBo);
        return Result.success(vo);
    }

}
