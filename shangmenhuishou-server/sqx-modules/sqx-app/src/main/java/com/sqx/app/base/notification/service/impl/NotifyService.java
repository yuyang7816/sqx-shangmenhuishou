package com.sqx.app.base.notification.service.impl;

import com.sqx.app.base.auth.enums.ClientEnum;
import com.sqx.app.base.notification.core.model.NotificationRequest;
import com.sqx.app.base.notification.core.service.INotificationService;
import com.sqx.app.base.notification.service.INotifyService;
import com.sqx.app.base.utils.SysConfigUtil;
import com.sqx.app.base.utils.wx.WxMaSubscribeMessage;
import com.sqx.app.utils.DateUtil;
import com.sqx.common.core.utils.GenUtil;
import com.sqx.domain.db.entity.UserEntity;
import com.sqx.domain.db.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author chengming
 * @since 2025/11/18
 */
@Slf4j
@Service
public class NotifyService implements INotifyService {

    @Resource
    private IUserService iUserService;
    @Resource
    private INotificationService iNotificationService;

    @Async
    @Override
    public void takeOrderNotify(Long userId, Long wasteUserId, String orderNo) {
        UserEntity userEntity = iUserService.getById(userId);
        if (Objects.isNull(userEntity)) {
            log.error("用户不存在，无法推送");
            return;
        }

        UserEntity wasteUser = iUserService.getById(wasteUserId);
        if (Objects.isNull(wasteUser)) {
            log.error("用户不存在，无法推送");
            return;
        }

        NotificationRequest request = new NotificationRequest();
        request.setUserId(userId);
        request.setTitle("回收员已接单");
        request.setContent("回收员已接单, 订单号: " + orderNo);
        request.setType(3);
        request.setCid(userEntity.getCid());
        request.setMsgNo(GenUtil.genMsgNo());
        request.setClientType(ClientEnum.USER.getCode());

        // 小程序消息
        if (StringUtils.isNotBlank(userEntity.getMaOpenId())) {
            String templateId = SysConfigUtil.getConfigValue("sys_user_take_order_template");
            WxMaSubscribeMessage wxMaSubscribeMessage = new WxMaSubscribeMessage();
            wxMaSubscribeMessage.setTemplateId(templateId);
            wxMaSubscribeMessage.setToUser(userEntity.getMaOpenId());

            List<WxMaSubscribeMessage.MsgData> msgDataList = Arrays.asList(
                    new WxMaSubscribeMessage.MsgData("thing10", "回收员已接单"),
                    new WxMaSubscribeMessage.MsgData("thing6", wasteUser.getUserName()),
                    new WxMaSubscribeMessage.MsgData("time7", DateUtil.now()),
                    new WxMaSubscribeMessage.MsgData("thing4", "回收员已接单")
            );

            wxMaSubscribeMessage.setData(msgDataList);
            request.setWxMaSubscribeMessage(wxMaSubscribeMessage);
        }

        iNotificationService.send(request);
    }

//    @Async
    @Override
    public void userCancelOrder(Long wasteUserId, String orderNo) {
        UserEntity userEntity = iUserService.getById(wasteUserId);
        if (Objects.isNull(userEntity)) {
            log.error("用户不存在，无法推送");
            return;
        }

        NotificationRequest request = new NotificationRequest();
        request.setUserId(wasteUserId);
        request.setTitle("用户取消接单");
        request.setContent("用户取消接单, 订单号: " + orderNo);
        request.setType(3);
        request.setCid(userEntity.getCid());
        request.setMsgNo(GenUtil.genMsgNo());
        request.setClientType(ClientEnum.WASTE.getCode());

        // 小程序消息
        if (StringUtils.isNotBlank(userEntity.getMaOpenId())) {
            String templateId = SysConfigUtil.getConfigValue("sys_waste_cancel_order_template");
            WxMaSubscribeMessage wxMaSubscribeMessage = new WxMaSubscribeMessage();
            wxMaSubscribeMessage.setTemplateId(templateId);
            wxMaSubscribeMessage.setToUser(userEntity.getMaOpenId());

            List<WxMaSubscribeMessage.MsgData> msgDataList = Arrays.asList(
                    new WxMaSubscribeMessage.MsgData("thing1", "用户取消订单"),
                    new WxMaSubscribeMessage.MsgData("character_string2", orderNo),
                    new WxMaSubscribeMessage.MsgData("thing3", ""),
                    new WxMaSubscribeMessage.MsgData("time4", DateUtil.now())
            );

            wxMaSubscribeMessage.setData(msgDataList);
            request.setWxMaSubscribeMessage(wxMaSubscribeMessage);
        }

        iNotificationService.send(request);
    }

//    @Async
    @Override
    public void wasteCancelOrder(Long userId, String orderNo) {
        UserEntity userEntity = iUserService.getById(userId);
        if (Objects.isNull(userEntity)) {
            log.error("用户不存在，无法推送");
            return;
        }

        NotificationRequest request = new NotificationRequest();
        request.setUserId(userId);
        request.setTitle("回收员取消接单");
        request.setContent("回收员取消接单, 订单号: " + orderNo);
        request.setType(3);
        request.setCid(userEntity.getCid());
        request.setMsgNo(GenUtil.genMsgNo());
        request.setClientType(ClientEnum.USER.getCode());

        // 小程序消息
        if (StringUtils.isNotBlank(userEntity.getMaOpenId())) {
            String templateId = SysConfigUtil.getConfigValue("sys_user_cancel_order_template");
            WxMaSubscribeMessage wxMaSubscribeMessage = new WxMaSubscribeMessage();
            wxMaSubscribeMessage.setTemplateId(templateId);
            wxMaSubscribeMessage.setToUser(userEntity.getMaOpenId());

            List<WxMaSubscribeMessage.MsgData> msgDataList = Arrays.asList(
                    new WxMaSubscribeMessage.MsgData("thing1", "回收员取消订单"),
                    new WxMaSubscribeMessage.MsgData("character_string2", orderNo),
                    new WxMaSubscribeMessage.MsgData("thing3", "回收员取消订单"),
                    new WxMaSubscribeMessage.MsgData("time4", DateUtil.now())
            );

            wxMaSubscribeMessage.setData(msgDataList);
            request.setWxMaSubscribeMessage(wxMaSubscribeMessage);
        }

        iNotificationService.send(request);
    }

    @Async
    @Override
    public void completeOrder(Long userId, Long wasteUserId, String orderNo) {
        // 用户
        UserEntity userEntity = iUserService.getById(userId);
        if (Objects.isNull(userEntity)) {
            log.error("用户不存在，无法推送");
            return;
        }

        NotificationRequest request = new NotificationRequest();
        request.setUserId(userId);
        request.setTitle("订单已完成");
        request.setContent("订单已完成, 订单号: " + orderNo);
        request.setType(3);
        request.setCid(userEntity.getCid());
        request.setMsgNo(GenUtil.genMsgNo());
        request.setClientType(ClientEnum.USER.getCode());

        // 小程序消息
        if (StringUtils.isNotBlank(userEntity.getMaOpenId())) {
            String templateId = SysConfigUtil.getConfigValue("sys_user_complete_order_template");
            WxMaSubscribeMessage wxMaSubscribeMessage = new WxMaSubscribeMessage();
            wxMaSubscribeMessage.setTemplateId(templateId);
            wxMaSubscribeMessage.setToUser(userEntity.getMaOpenId());

            List<WxMaSubscribeMessage.MsgData> msgDataList = Arrays.asList(
                    new WxMaSubscribeMessage.MsgData("character_string1", orderNo),
                    new WxMaSubscribeMessage.MsgData("time4", DateUtil.now()),
                    new WxMaSubscribeMessage.MsgData("thing5", "订单已完成")
            );

            wxMaSubscribeMessage.setData(msgDataList);
            request.setWxMaSubscribeMessage(wxMaSubscribeMessage);
        }

        iNotificationService.send(request);

        // 回收员
        UserEntity wasteUser = iUserService.getById(wasteUserId);
        if (Objects.isNull(wasteUser)) {
            log.error("用户不存在，无法推送");
            return;
        }

        NotificationRequest wasteRequest = new NotificationRequest();
        wasteRequest.setUserId(wasteUserId);
        wasteRequest.setTitle("订单已完成");
        wasteRequest.setContent("订单已完成, 订单号: " + orderNo);
        wasteRequest.setType(3);
        wasteRequest.setCid(wasteUser.getCid());
        wasteRequest.setMsgNo(GenUtil.genMsgNo());
        wasteRequest.setClientType(ClientEnum.WASTE.getCode());

        // 小程序消息
        if (StringUtils.isNotBlank(wasteUser.getMaOpenId())) {
            String wasteTemplateId = SysConfigUtil.getConfigValue("sys_waste_complete_order_template");
            WxMaSubscribeMessage wasteWxMaSubscribeMessage = new WxMaSubscribeMessage();
            wasteWxMaSubscribeMessage.setTemplateId(wasteTemplateId);
            wasteWxMaSubscribeMessage.setToUser(wasteUser.getMaOpenId());

            List<WxMaSubscribeMessage.MsgData> wasteMsgDataList = Arrays.asList(
                    new WxMaSubscribeMessage.MsgData("character_string1", orderNo),
                    new WxMaSubscribeMessage.MsgData("time4", DateUtil.now()),
                    new WxMaSubscribeMessage.MsgData("thing5", "订单已完成")
            );

            wasteWxMaSubscribeMessage.setData(wasteMsgDataList);
            wasteRequest.setWxMaSubscribeMessage(wasteWxMaSubscribeMessage);
        }

        iNotificationService.send(wasteRequest);
    }
}
