package com.sqx.common.pay.platform.wechat.utils;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.sqx.common.pay.platform.wechat.domain.WxPayConfigParam;

/**
 * @author chengming
 * @since 2025/4/18
 */
public class WxPayConfigUtil {

    /**
     * 获取微信支付配置
     * @param wxPayConfigParam 微信支付配置参数
     * @return 微信支付配置
     */
    public static WxPayConfig getWxPayConfig(WxPayConfigParam wxPayConfigParam) {
        WxPayConfig payConfig = new WxPayConfig();

        payConfig.setAppId(wxPayConfigParam.getAppId());
        payConfig.setMchId(wxPayConfigParam.getMchId());
        payConfig.setMchKey(wxPayConfigParam.getMchKey());
        payConfig.setNotifyUrl(wxPayConfigParam.getNotifyUrl());
        payConfig.setTradeType(wxPayConfigParam.getTradeType());

        return payConfig;
    }
}
