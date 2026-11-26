package com.sqx.app.base.notification.service;

/**
 * @author chengming
 * @since 2025/11/18
 *
 * 业务通知
 */
public interface INotifyService {

    /**
     * 师傅接单通知
     * @param userId 用户ID
     * @param wasteUserId 回收员用户ID
     * @param orderNo 订单号
     */
    void takeOrderNotify(Long userId, Long wasteUserId, String orderNo);

    /**
     * 用户取消订单，通知回收员
     * @param wasteUserId 回收员用户ID
     * @param orderNo 订单号
     */
    void userCancelOrder(Long wasteUserId, String orderNo);

    /**
     * 回收员取消订单，通知用户
     * @param userId 用户ID
     * @param orderNo 订单号
     */
    void wasteCancelOrder(Long userId, String orderNo);

    /**
     * 订单完成，通知用户和回收员
     * @param userId 用户ID
     * @param wasteUserId 回收员ID
     * @param orderNo 订单号
     */
    void completeOrder(Long userId, Long wasteUserId, String orderNo);
}
