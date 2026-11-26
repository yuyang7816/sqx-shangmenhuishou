package com.sqx.app.messages.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.app.messages.db.mapper.MessagesMapper;
import com.sqx.app.messages.db.service.IMessagesService;
import com.sqx.app.messages.domain.entity.Messages;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 消息信息表 服务实现类
 * </p>
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-08-15 16:28:04
 */
@Service
public class IMessagesServiceImpl extends ServiceImpl<MessagesMapper, Messages> implements IMessagesService {

    @Override
    public void sendCancelOrder(Long userId, Long orderId, String orderNo) {
        Messages messages = new Messages();
        messages.setTitle("用户取消订单");
        messages.setContent(String.format("用户已取消订单[%s]", orderNo));
        messages.setType(1);
        messages.setUserId(userId);
        messages.setDataId(orderId);
        messages.setIsRead(0);

        this.save(messages);
    }

    @Override
    public void saveCancelOrder(Long userId, Long orderId, String orderNo) {
        Messages messages = new Messages();
        messages.setTitle("取消订单");
        messages.setContent(String.format("已取消订单[%s]", orderNo));
        messages.setType(1);
        messages.setUserId(userId);
        messages.setDataId(orderId);
        messages.setIsRead(0);

        this.save(messages);
    }

    @Override
    public void saveWasteCancelOrder(Long userId, Long orderId, String orderNo) {
        Messages messages = new Messages();
        messages.setTitle("取消接单");
        messages.setContent(String.format("已取消接单[%s]", orderNo));
        messages.setType(1);
        messages.setUserId(userId);
        messages.setDataId(orderId);
        messages.setIsRead(0);

        this.save(messages);
    }

    @Override
    public void sendUserWasteCancelOrder(Long userId, Long orderId, String orderNo) {
        Messages messages = new Messages();
        messages.setTitle("回收员取消接单");
        messages.setContent(String.format("回收员已取消接单[%s]", orderNo));
        messages.setType(1);
        messages.setUserId(userId);
        messages.setDataId(orderId);
        messages.setIsRead(0);

        this.save(messages);
    }

    @Override
    public void updateStatus(Long userId, int read) {
        LambdaUpdateWrapper<Messages> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .eq(Messages::getUserId, userId)
                .set(Messages::getIsRead, read);

        this.update(new Messages(), updateWrapper);
    }
}
