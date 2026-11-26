package com.sqx.app.index.service;

import com.sqx.app.index.domain.bo.StatisticsBo;
import com.sqx.app.index.domain.vo.IndexStatisticsVo;
import com.sqx.app.index.domain.vo.OrderStatisticsVo;
import com.sqx.app.index.domain.vo.UserStatisticsVo;
import com.sqx.app.referral.withdraw.domain.vo.WithdrawStatisticsVo;

/**
 * @author chengming
 * @since 2025/10/14
 */
public interface IndexService {
    IndexStatisticsVo indexStatistics();

    UserStatisticsVo userStatistics(StatisticsBo statisticsBo);

    OrderStatisticsVo orderStatistics(StatisticsBo statisticsBo);

    WithdrawStatisticsVo withdrawStatistics(StatisticsBo statisticsBo);
}
