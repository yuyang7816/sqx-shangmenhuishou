package com.sqx.app.index.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.app.base.user.enums.UserType;
import com.sqx.app.index.domain.bo.StatisticsBo;
import com.sqx.app.index.domain.vo.IndexStatisticsVo;
import com.sqx.app.index.domain.vo.OrderStatisticsVo;
import com.sqx.app.index.domain.vo.UserStatisticsVo;
import com.sqx.app.index.service.IndexService;
import com.sqx.app.referral.withdraw.db.service.IWithdrawRecordService;
import com.sqx.app.referral.withdraw.domain.entity.WithdrawRecord;
import com.sqx.app.referral.withdraw.domain.vo.WithdrawStatisticsVo;
import com.sqx.app.utils.DateUtil;
import com.sqx.app.utils.model.StartAndEndTime;
import com.sqx.app.waste.order.item.db.service.IWasteOrderItemService;
import com.sqx.app.waste.order.order.db.service.IWasteOrderService;
import com.sqx.app.waste.order.order.domain.entity.WasteOrder;
import com.sqx.app.waste.order.order.enums.OrderStatus;
import com.sqx.domain.db.entity.UserEntity;
import com.sqx.domain.db.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author chengming
 * @since 2025/10/14
 */
@Service
public class IndexServiceImpl implements IndexService {

    @Resource
    private IUserService iUserService;
    @Resource
    private IWasteOrderService iWasteOrderService;
    @Resource
    private IWasteOrderItemService iWasteOrderItemService;
    @Resource
    private IWithdrawRecordService iWithdrawRecordService;

    @Override
    public IndexStatisticsVo indexStatistics() {
        StartAndEndTime today = DateUtil.today();
        StartAndEndTime month = DateUtil.month();
        StartAndEndTime year = DateUtil.year();

        // 总用户数
        Long userCount = iUserService.count();

        Long todayUserCount = getUserCount(today.getStartTime(), today.getEndTime());
        Long monthUserCount = getUserCount(month.getStartTime(), month.getEndTime());
        Long yearUserCount = getUserCount(year.getStartTime(), year.getEndTime());

        Long normalUserCount = getUserTypeCount(UserType.NORMAL.getCode());
        Long wasteUserCount = getUserTypeCount(UserType.WASTE.getCode());

        Long orderCount = getOrderCount(null);
        Long dayOrderCount = getOrderCount(today);
        Long monthOrderCount = getOrderCount(month);
        Long yearOrderCount = getOrderCount(year);

        BigDecimal amount = getOrderAmount(null);
        BigDecimal dayAmount = getOrderAmount(today);
        BigDecimal monthAmount = getOrderAmount(month);
        BigDecimal yearAmount = getOrderAmount(year);

        IndexStatisticsVo vo = new IndexStatisticsVo();
        vo.setUserCount(userCount);
        vo.setTodayUserCount(todayUserCount);
        vo.setMonthUserCount(monthUserCount);
        vo.setYearUserCount(yearUserCount);
        vo.setNormalUserCount(normalUserCount);
        vo.setWasteUserCount(wasteUserCount);
        vo.setOrderCount(orderCount);
        vo.setDayOrderCount(dayOrderCount);
        vo.setMonthOrderCount(monthOrderCount);
        vo.setYearOrderCount(yearOrderCount);
        vo.setAmount(amount);
        vo.setDayAmount(dayAmount);
        vo.setMonthAmount(monthAmount);
        vo.setYearAmount(yearAmount);

        return vo;
    }

    @Override
    public UserStatisticsVo userStatistics(StatisticsBo statisticsBo) {
        statisticsBo.initFormattedDateRange();

        Long userCount = iUserService.count();
        Long normalUserCount = getUserCountByType(statisticsBo.getStartTime(), statisticsBo.getEndTime(), UserType.NORMAL.getCode());
        Long wasteUserCount = getUserCountByType(statisticsBo.getStartTime(), statisticsBo.getEndTime(), UserType.WASTE.getCode());

        UserStatisticsVo vo = new UserStatisticsVo();
        vo.setUserCount(userCount);
        vo.setNormalUserCount(normalUserCount);
        vo.setWasteUserCount(wasteUserCount);
        return vo;
    }

    @Override
    public OrderStatisticsVo orderStatistics(StatisticsBo statisticsBo) {
        statisticsBo.initFormattedDateRange();

        LambdaQueryWrapper<WasteOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.between(WasteOrder::getCreateTime, statisticsBo.getStartTime(), statisticsBo.getEndTime());
        List<WasteOrder> orders = iWasteOrderService.list(queryWrapper);
        Map<Integer, Long> map = orders.stream().collect(Collectors.groupingBy(WasteOrder::getStatus, Collectors.counting()));

        List<OrderStatisticsVo.OrderStatistics> orderStatisticsList = new ArrayList<>();
        for (Integer status : OrderStatus.ALL_CODES) {
            OrderStatisticsVo.OrderStatistics orderStatistics = new OrderStatisticsVo.OrderStatistics();
            orderStatistics.setStatus(status);
            orderStatistics.setDesc(OrderStatus.getDescByCode(status));
            orderStatistics.setCount(map.getOrDefault(status, 0L));

            orderStatisticsList.add(orderStatistics);
        }

        OrderStatisticsVo vo = new OrderStatisticsVo();
        vo.setTotalCount((long) orders.size());
        vo.setOrderStatisticsList(orderStatisticsList);

        return vo;
    }

    @Override
    public WithdrawStatisticsVo withdrawStatistics(StatisticsBo statisticsBo) {
        statisticsBo.initFormattedDateRange();

        BigDecimal totalAmount = iWithdrawRecordService.sumAmount(statisticsBo.getStartTime(), statisticsBo.getEndTime());

        QueryWrapper<WithdrawRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().between(WithdrawRecord::getCreateTime, statisticsBo.getStartTime(), statisticsBo.getEndTime());
        Long totalCount = iWithdrawRecordService.count(queryWrapper);

        queryWrapper.lambda().eq(WithdrawRecord::getStatus, 1);
        Long waitCount = iWithdrawRecordService.count(queryWrapper);

        WithdrawStatisticsVo vo = new WithdrawStatisticsVo();
        vo.setTotalAmount(totalAmount);
        vo.setTotalCount(totalCount);
        vo.setWaitCount(waitCount);

        return vo;
    }

    private Long getUserCountByType(LocalDateTime startTime, LocalDateTime endTime, Integer type) {
        if (Objects.isNull(startTime) && Objects.isNull(endTime)) {
            return iUserService.count();
        }

        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(UserEntity::getUserType, type)
                .between(UserEntity::getCreateTime, startTime, endTime);

        return iUserService.count(queryWrapper);
    }

    private Long getUserCount(LocalDateTime startTime, LocalDateTime endTime) {
        if (Objects.isNull(startTime) && Objects.isNull(endTime)) {
            return iUserService.count();
        }

        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.between(UserEntity::getCreateTime, startTime, endTime);

        return iUserService.count(queryWrapper);
    }

    private BigDecimal getOrderAmount(StartAndEndTime startAndEndTime) {
        return iWasteOrderItemService.sumAmount(startAndEndTime);
    }

    private Long getOrderCount(StartAndEndTime startAndEndTime) {
        if (Objects.isNull(startAndEndTime)) {
            return iWasteOrderService.count();
        }

        LambdaQueryWrapper<WasteOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.between(WasteOrder::getCreateTime, startAndEndTime.getStartTime(), startAndEndTime.getEndTime());

        return iWasteOrderService.count(queryWrapper);
    }

    /**
     * 指定类型用户数
     *
     * @param userType 用户类型
     */
    private Long getUserTypeCount(Integer userType) {
        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserEntity::getUserType, userType);

        return iUserService.count(queryWrapper);
    }
}
