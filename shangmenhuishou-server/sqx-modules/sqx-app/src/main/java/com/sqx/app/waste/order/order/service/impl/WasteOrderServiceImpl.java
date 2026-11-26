package com.sqx.app.waste.order.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.app.address.db.service.IAddressService;
import com.sqx.app.address.domain.entity.Address;
import com.sqx.app.base.notification.service.INotifyService;
import com.sqx.app.base.utils.SysConfigUtil;
import com.sqx.app.messages.db.service.IMessagesService;
import com.sqx.app.waste.category.db.service.IWasteCategoryService;
import com.sqx.app.waste.category.domain.entity.WasteCategory;
import com.sqx.app.waste.item.db.service.IWasteItemService;
import com.sqx.app.waste.item.domain.entity.WasteItem;
import com.sqx.app.waste.order.item.db.service.IWasteOrderItemService;
import com.sqx.app.waste.order.item.domain.entity.WasteOrderItem;
import com.sqx.app.waste.order.order.db.service.IWasteOrderService;
import com.sqx.app.waste.order.order.domain.bo.*;
import com.sqx.app.waste.order.order.domain.entity.WasteOrder;
import com.sqx.app.waste.order.order.domain.vo.TakeOrderDataVo;
import com.sqx.app.waste.order.order.domain.vo.WasteOrderDataVo;
import com.sqx.app.waste.order.order.domain.vo.WasteOrderMyVo;
import com.sqx.app.waste.order.order.domain.vo.WasteOrderVo;
import com.sqx.app.waste.order.order.enums.OrderStatus;
import com.sqx.app.waste.order.order.service.WasteOrderService;
import com.sqx.common.core.exception.BizException;
import com.sqx.common.core.utils.GenUtil;
import com.sqx.common.mybatis.utils.PageUtil;
import com.sqx.common.shiro.utils.UserContextHolder;
import com.sqx.domain.db.entity.UserEntity;
import com.sqx.domain.db.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.shiro.realm.CachingRealm;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 订单 服务实现类
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-03 14:28:34
 */
@Slf4j
@Service
public class WasteOrderServiceImpl implements WasteOrderService {

    @Resource
    private IWasteOrderService iWasteOrderService;
    @Resource
    private IWasteOrderItemService iWasteOrderItemService;
    @Resource
    private IMessagesService iMessagesService;
    @Resource
    private IAddressService iAddressService;
    @Resource
    private IWasteItemService iWasteItemService;
    @Resource
    private IWasteCategoryService iWasteCategoryService;
    @Resource
    private IUserService iUserService;
    @Resource
    private INotifyService iNotifyService;
    private CachingRealm cachingRealm;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveInfo(WasteOrderSaveBo wasteOrderSaveBo) {
        if (wasteOrderSaveBo.getAppointmentTime().isBefore(LocalDateTime.now())) {
            throw new BizException("预约时间不能早于当前时间");
        }

        WasteOrder wasteOrder = new WasteOrder();
        BeanUtils.copyProperties(wasteOrderSaveBo, wasteOrder);

        wasteOrder.setOrderNo(GenUtil.genOrderNo());
        wasteOrder.setStatus(OrderStatus.PENDING_ACCEPT.getCode());
        iWasteOrderService.save(wasteOrder);

        // 预约物品列表
        if (CollectionUtils.isNotEmpty(wasteOrderSaveBo.getOrderItems())) {
            for (WasteOrderItem orderItem : wasteOrderSaveBo.getOrderItems()) {
                orderItem.setOrderId(wasteOrder.getId());
            }

            iWasteOrderItemService.saveBatch(wasteOrderSaveBo.getOrderItems());
        }
    }

    @Override
    public IPage<WasteOrderVo> pageInfo(WasteOrderPageBo wasteOrderPageBo) {
        Page<WasteOrder> initPage = PageUtil.initPage(wasteOrderPageBo.getPage(), wasteOrderPageBo.getLimit());

        IPage<WasteOrderVo> voIPage = iWasteOrderService.pageInfo(initPage, wasteOrderPageBo);

        if (CollectionUtils.isNotEmpty(voIPage.getRecords())) {
            List<Long> orderIds = voIPage.getRecords().stream()
                    .map(WasteOrder::getId)
                    .collect(Collectors.toList());

            List<WasteOrderItem> orderItems = iWasteOrderItemService.list(new LambdaQueryWrapper<WasteOrderItem>()
                    .in(WasteOrderItem::getOrderId, orderIds)
            );

            Map<Long, List<WasteOrderItem>> orderItemMap = orderItems.stream()
                    .collect(Collectors.groupingBy(WasteOrderItem::getOrderId));

            for (WasteOrderVo record : voIPage.getRecords()) {
                record.setOrderItems(orderItemMap.getOrDefault(record.getId(), Collections.emptyList()));
            }
        }

        return voIPage;
    }

    @Override
    public WasteOrderVo info(WasteOrderInfoBo wasteOrderInfoBo) {
        WasteOrder wasteOrder = iWasteOrderService.getById(wasteOrderInfoBo.getId());
        if (Objects.isNull(wasteOrder)) {
            throw new BizException("信息不存在");
        }

        List<WasteOrderItem> items = iWasteOrderItemService.listByOrderId(wasteOrder.getId());

        WasteOrderVo vo = new WasteOrderVo();
        BeanUtils.copyProperties(wasteOrder, vo);
        vo.setOrderItems(items);

        // 接单人信息
        if (Objects.nonNull(wasteOrder.getWasteUserId())) {
            UserEntity userEntity = iUserService.getById(wasteOrder.getWasteUserId());
            if (Objects.nonNull(userEntity)) {
                vo.setWasteUser(userEntity);
            }
        }

        // 发单人信息
        if (Objects.nonNull(wasteOrder.getUserId())) {
            UserEntity userEntity = iUserService.getById(wasteOrder.getUserId());
            if (Objects.nonNull(userEntity)) {
                vo.setUserInfo(userEntity);
            }
        }

        return vo;
    }

    @Override
    public void updateInfo(WasteOrderUpdateBo wasteOrderUpdateBo) {
        WasteOrder wasteOrder = new WasteOrder();
        BeanUtils.copyProperties(wasteOrderUpdateBo, wasteOrder);

        iWasteOrderService.updateById(wasteOrder);
    }

    @Override
    public void delete(WasteOrderInfoBo wasteOrderInfoBo) {
        iWasteOrderService.removeById(wasteOrderInfoBo.getId());
    }

    @Override
    public void take(WasteOrderTakeBo wasteOrderTakeBo) {
        WasteOrder wasteOrder = iWasteOrderService.getById(wasteOrderTakeBo.getOrderId());
        if (Objects.isNull(wasteOrder)) {
            throw new BizException("订单不存在");
        }

        // 校验回收员保证金
        if (!checkDeposit(wasteOrderTakeBo.getUserId())) {
            throw new BizException("保证金余额不足，无法接单");
        }

        LambdaUpdateWrapper<WasteOrder> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .eq(WasteOrder::getId, wasteOrderTakeBo.getOrderId())
                .set(WasteOrder::getStatus, OrderStatus.PENDING_SERVICE.getCode())
                .set(WasteOrder::getWasteUserId, wasteOrderTakeBo.getUserId())
                .set(WasteOrder::getWasteTime, LocalDateTime.now());

        iWasteOrderService.update(new WasteOrder(), updateWrapper);

        // 给用户发消息
        iNotifyService.takeOrderNotify(wasteOrder.getUserId(), wasteOrderTakeBo.getUserId(), wasteOrder.getOrderNo());
    }

    private boolean checkDeposit(Long userId) {
        String deposit = SysConfigUtil.getConfigValue("biz_enable_deposit");
        if (!"是".equals(deposit)) {
            return true;
        }

        String defaultDepositAmount = SysConfigUtil.getConfigValue("biz_deposit_amount");
        UserEntity userEntity = iUserService.getById(userId);
        if (Objects.isNull(userEntity)) {
            throw new BizException("用户信息不存在");
        }

        if (Objects.isNull(userEntity.getDeposit())) {
            return false;
        }

        return userEntity.getDeposit().compareTo(new BigDecimal(defaultDepositAmount)) >= 0;
    }

    @Override
    public List<WasteOrderMyVo> myOrder() {
        LambdaQueryWrapper<WasteOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WasteOrder::getUserId, UserContextHolder.getAppUserId());
        List<WasteOrder> wasteOrderList = iWasteOrderService.list(queryWrapper);

        Map<Integer, Long> wasteOrderCountMap = new HashMap<>();
        for (WasteOrder wasteOrder : wasteOrderList) {
            // 统计每个 status 出现的次数：如果不存在则初始化为1，存在则+1
            wasteOrderCountMap.merge(wasteOrder.getStatus(), 1L, Long::sum);
        }

        List<Integer> allStatuses = OrderStatus.ALL_CODES;
        List<WasteOrderMyVo> voList = new ArrayList<>();
        for (Integer status : allStatuses) {
            Long count = wasteOrderCountMap.getOrDefault(status, 0L); // 不存在就给 0
            voList.add(new WasteOrderMyVo(status, count));
        }

        return voList;
    }

    @Override
    public TakeOrderDataVo takeOrderData() {
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        // 总接单
        Long allCount = takeOrderCount(UserContextHolder.getAppUserId(), startTime, endTime);

        // 今日接单
        startTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        endTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        Long todayCount = takeOrderCount(UserContextHolder.getAppUserId(), startTime, endTime);

        // 本月接单
        YearMonth yearMonth = YearMonth.from(LocalDateTime.now());
        startTime = yearMonth.atDay(1).atStartOfDay();
        endTime = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);
        Long monthCount = takeOrderCount(UserContextHolder.getAppUserId(), startTime, endTime);

        TakeOrderDataVo vo = new TakeOrderDataVo();
        vo.setAllCount(allCount);
        vo.setTodayCount(todayCount);
        vo.setMonthCount(monthCount);

        return vo;
    }

    @Override
    public List<WasteOrderDataVo> wasteOrderData() {
        LambdaQueryWrapper<WasteOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WasteOrder::getWasteUserId, UserContextHolder.getAppUserId());
        List<WasteOrder> wasteOrderList = iWasteOrderService.list(queryWrapper);

        Map<Integer, Long> wasteOrderCountMap = new HashMap<>();
        for (WasteOrder wasteOrder : wasteOrderList) {
            // 统计每个 status 出现的次数：如果不存在则初始化为1，存在则+1
            wasteOrderCountMap.merge(wasteOrder.getStatus(), 1L, Long::sum);
        }

        List<Integer> allStatuses = OrderStatus.ALL_CODES;
        List<WasteOrderDataVo> voList = new ArrayList<>();
        for (Integer status : allStatuses) {
            Long count = wasteOrderCountMap.getOrDefault(status, 0L); // 不存在就给 0
            voList.add(new WasteOrderDataVo(status, count));
        }

        return voList;
    }

    @Override
    public void cancel(WasteOrderCancelBo wasteOrderCancelBo) {
        WasteOrder wasteOrder = iWasteOrderService.getById(wasteOrderCancelBo.getOrderId());
        if (Objects.isNull(wasteOrder)) {
            throw new BizException("订单信息不存在");
        }

        if (Objects.equals(OrderStatus.IN_PROGRESS.getCode(), wasteOrder.getStatus()) ||
                Objects.equals(OrderStatus.COMPLETED.getCode(), wasteOrder.getStatus()) ||
                Objects.equals(OrderStatus.CANCELLED.getCode(), wasteOrder.getStatus())
        ) {
            throw new BizException("当前不能取消订单");
        }

        LambdaUpdateWrapper<WasteOrder> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .eq(WasteOrder::getId, wasteOrderCancelBo.getOrderId())
                .set(WasteOrder::getStatus, OrderStatus.CANCELLED.getCode());

        iWasteOrderService.update(new WasteOrder(), updateWrapper);

        // 如果已被接单，需要给师傅发消息
        if (Objects.equals(OrderStatus.PENDING_SERVICE.getCode(), wasteOrder.getStatus())) {
            iNotifyService.userCancelOrder(wasteOrder.getWasteUserId(), wasteOrder.getOrderNo());
        }
    }

    @Override
    public IPage<WasteOrderVo> pool(Long page, Long limit, String city) {
        Page<WasteOrder> initPage = PageUtil.initPage(page, limit);

        LambdaQueryWrapper<WasteOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(WasteOrder::getStatus, OrderStatus.PENDING_ACCEPT.getCode())
                .eq(WasteOrder::getCity, city)
                .orderByDesc(WasteOrder::getCreateTime);

        IPage<WasteOrder> orderPage = iWasteOrderService.page(initPage, queryWrapper);
        List<WasteOrderVo> voList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(orderPage.getRecords())) {
            List<Long> orderIds = orderPage.getRecords().stream()
                    .map(WasteOrder::getId)
                    .collect(Collectors.toList());

            List<WasteOrderItem> orderItems = iWasteOrderItemService.list(new LambdaQueryWrapper<WasteOrderItem>()
                    .in(WasteOrderItem::getOrderId, orderIds)
            );

            Map<Long, List<WasteOrderItem>> orderItemMap = orderItems.stream()
                    .collect(Collectors.groupingBy(WasteOrderItem::getOrderId));

            for (WasteOrder wasteOrder : orderPage.getRecords()) {
                WasteOrderVo vo = new WasteOrderVo();
                BeanUtils.copyProperties(wasteOrder, vo);

                vo.setOrderItems(orderItemMap.getOrDefault(wasteOrder.getId(), Collections.emptyList()));
                voList.add(vo);
            }
        }

        IPage<WasteOrderVo> voIPage = PageUtil.copyPage(orderPage, voList);
        return voIPage;
    }

    @Override
    public WasteOrderVo information(WasteOrderInformationBo wasteOrderInformationBo) {
        WasteOrderVo vo = iWasteOrderService.information(wasteOrderInformationBo.getId(), wasteOrderInformationBo.getLat(), wasteOrderInformationBo.getLng());

        List<WasteOrderItem> items = iWasteOrderItemService.listByOrderId(wasteOrderInformationBo.getId());
        vo.setOrderItems(items);
        return vo;
    }

    @Override
    public void complete(WasteOrderCompleteBo wasteOrderCompleteBo) {
        WasteOrder wasteOrder = iWasteOrderService.getById(wasteOrderCompleteBo.getOrderId());
        if (Objects.isNull(wasteOrder)) {
            throw new BizException("订单信息不存在");
        }

        if (!Objects.equals(OrderStatus.IN_PROGRESS.getCode(), wasteOrder.getStatus())) {
            throw new BizException("订单状态异常，不能完成该订单");
        }

        LambdaUpdateWrapper<WasteOrder> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .eq(WasteOrder::getId, wasteOrderCompleteBo.getOrderId())
                .set(WasteOrder::getStatus, OrderStatus.COMPLETED.getCode());

        iWasteOrderService.update(new WasteOrder(), updateWrapper);

        // 通知用户和回收员
        iNotifyService.completeOrder(wasteOrder.getUserId(), wasteOrder.getWasteUserId(), wasteOrder.getOrderNo());
    }

    @Override
    public void start(WasteOrderStartBo wasteOrderStartBo) {
        WasteOrder wasteOrder = iWasteOrderService.getById(wasteOrderStartBo.getOrderId());
        if (Objects.isNull(wasteOrder)) {
            throw new BizException("订单信息不存在");
        }

        if (!Objects.equals(OrderStatus.PENDING_SERVICE.getCode(), wasteOrder.getStatus())) {
            throw new BizException("订单状态异常，不能完成该订单");
        }

        LambdaUpdateWrapper<WasteOrder> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .eq(WasteOrder::getId, wasteOrderStartBo.getOrderId())
                .set(WasteOrder::getStatus, OrderStatus.IN_PROGRESS.getCode());

        iWasteOrderService.update(new WasteOrder(), updateWrapper);
    }

    @Override
    public void stop(WasteOrderStopBo wasteOrderStopBo) {
        WasteOrder wasteOrder = iWasteOrderService.getById(wasteOrderStopBo.getOrderId());
        if (Objects.isNull(wasteOrder)) {
            throw new BizException("订单信息不存在");
        }

        if (!Objects.equals(wasteOrderStopBo.getUserId(), wasteOrder.getWasteUserId())) {
            throw new BizException("非本人订单，无法停止服务该订单");
        }

        if (!Objects.equals(OrderStatus.PENDING_SERVICE.getCode(), wasteOrder.getStatus())) {
            throw new BizException("不能停止服务该订单");
        }

        LambdaUpdateWrapper<WasteOrder> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .eq(WasteOrder::getId, wasteOrderStopBo.getOrderId())
                .set(WasteOrder::getStatus, OrderStatus.PENDING_ACCEPT.getCode());

        iWasteOrderService.update(new WasteOrder(), updateWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void itemOrder(ItemOrderBo itemOrderBo) {
        if (itemOrderBo.getAppointmentTime().isBefore(LocalDateTime.now())) {
            throw new BizException("预约时间不能早于当前时间");
        }

        Address address = iAddressService.getById(itemOrderBo.getAddressId());
        if (Objects.isNull(address)) {
            throw new BizException("用户地址不存在");
        }

        WasteItem wasteItem = iWasteItemService.getById(itemOrderBo.getItemId());
        if (Objects.isNull(wasteItem)) {
            throw new BizException("回收项目不存在");
        }

        WasteCategory wasteCategory = iWasteCategoryService.getById(wasteItem.getCategoryId());
        if (Objects.isNull(wasteCategory)) {
            log.error("类型不存在: [{}]", wasteItem.getCategoryId());
            throw new BizException("分类不存在");
        }

        WasteCategory parentCategory = iWasteCategoryService.getById(wasteCategory.getParentId());
        if (Objects.isNull(parentCategory)) {
            log.error("父类型不存在: [{}]", wasteCategory.getParentId());
            throw new BizException("分类不存在");
        }

        // 订单信息
        String orderNo = GenUtil.genOrderNo();
        WasteOrder order = new WasteOrder();
        order.setUserId(itemOrderBo.getUserId());
        order.setOrderNo(orderNo);
        order.setStatus(OrderStatus.PENDING_ACCEPT.getCode());
        order.setAddressName(address.getName());
        order.setAddressPhone(address.getPhone());
        order.setProvince(address.getProvince());
        order.setCity(address.getCity());
        order.setDistrict(address.getDistrict());
        order.setAddress(address.getAddress());
        order.setNote(itemOrderBo.getNote());
        order.setAppointmentTime(itemOrderBo.getAppointmentTime());
        iWasteOrderService.save(order);

        // 订单物品
        WasteOrderItem orderItem = new WasteOrderItem();
        orderItem.setOrderId(order.getId());
        orderItem.setNum(itemOrderBo.getNum());
        orderItem.setCategoryId(parentCategory.getId());
        orderItem.setCategoryName(parentCategory.getName());
        orderItem.setCategoryChildrenId(wasteItem.getCategoryId());
        orderItem.setCategoryChildrenName(wasteCategory.getName());
        orderItem.setPrice(wasteItem.getPrice().toString());
        orderItem.setUnit(itemOrderBo.getUnit());
        orderItem.setPrice(wasteItem.getPrice().multiply(BigDecimal.valueOf(wasteItem.getAppointmentNum())).toString());

        iWasteOrderItemService.save(orderItem);
    }

    @Override
    public void wasteCancel(WasteOrderCancelBo wasteOrderCancelBo) {
        WasteOrder wasteOrder = iWasteOrderService.getById(wasteOrderCancelBo.getOrderId());
        if (Objects.isNull(wasteOrder)) {
            throw new BizException("订单信息不存在");
        }

        if (Objects.equals(OrderStatus.IN_PROGRESS.getCode(), wasteOrder.getStatus()) ||
                Objects.equals(OrderStatus.COMPLETED.getCode(), wasteOrder.getStatus()) ||
                Objects.equals(OrderStatus.CANCELLED.getCode(), wasteOrder.getStatus())
        ) {
            throw new BizException("当前不能取消订单");
        }

        LambdaUpdateWrapper<WasteOrder> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .eq(WasteOrder::getId, wasteOrderCancelBo.getOrderId())
                .set(WasteOrder::getStatus, OrderStatus.PENDING_ACCEPT.getCode());

        iWasteOrderService.update(new WasteOrder(), updateWrapper);

        // 给用户发消息
        iNotifyService.wasteCancelOrder(wasteOrder.getUserId(), wasteOrder.getOrderNo());
    }

    private Long takeOrderCount(Long userId, LocalDateTime startTime, LocalDateTime endTime) {
        QueryWrapper<WasteOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(WasteOrder::getWasteUserId, userId)
                .eq(WasteOrder::getStatus, OrderStatus.COMPLETED.getCode());

        if (Objects.nonNull(startTime) && Objects.nonNull(endTime)) {
            queryWrapper.lambda().between(WasteOrder::getWasteTime, startTime, endTime);
        }

        return iWasteOrderService.count(queryWrapper);
    }
}
