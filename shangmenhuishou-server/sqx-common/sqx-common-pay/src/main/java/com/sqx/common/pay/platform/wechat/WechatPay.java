package com.sqx.common.pay.platform.wechat;

import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.sqx.common.pay.platform.wechat.domain.WxPayConfigParam;
import com.sqx.common.pay.platform.wechat.utils.WxPayConfigUtil;

/**
 * @author chengming
 * @since 2025/4/18
 */
public class WechatPay {

    /**
     * 拉起微信支付
     * @param outTradeNo 订单号
     * @param amount 金额
     * @param subject 订单标题
     * @param wxPayConfigParam 微信支付配置参数
     * @return 跳转链接
     */
    public static <T> T pay(String outTradeNo, String amount, String subject, WxPayConfigParam wxPayConfigParam) throws WxPayException {
        WxPayConfig payConfig = WxPayConfigUtil.getWxPayConfig(wxPayConfigParam);
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);

        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
        orderRequest.setBody(subject);
        orderRequest.setOutTradeNo(outTradeNo);
        orderRequest.setTotalFee(BaseWxPayRequest.yuanToFen(amount)); //元转成分
        orderRequest.setSpbillCreateIp("127.0.0.1");
        orderRequest.setNotifyUrl(wxPayConfigParam.getNotifyUrl());
        orderRequest.setTradeType(wxPayConfigParam.getTradeType());
        orderRequest.setOpenid(wxPayConfigParam.getOpenId());

        return wxPayService.createOrder(orderRequest);
    }
}
