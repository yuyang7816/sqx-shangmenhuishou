package com.sqx.app.messages.db.service;

import com.sqx.app.messages.domain.entity.Messages;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 消息信息表 服务类
 * </p>
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-08-15 16:28:04
 */
public interface IMessagesService extends IService<Messages> {

    void sendCancelOrder(Long userId, Long orderId, String orderNo);

    void saveCancelOrder(Long userId, Long orderId, String orderNo);

    void saveWasteCancelOrder(Long userId, Long orderId, String orderNo);

    void sendUserWasteCancelOrder(Long userId, Long orderId, String orderNo);

    void updateStatus(Long userId, int read);
}
