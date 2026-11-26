package com.sqx.common.pay.platform.wechat.domain;

/**
 * @author chengming
 * @since 2025/4/21
 */
public class WxPayConfigParam {

    /**
     * 应用id
     */
    private String appId;

    /**
     * 商户id
     */
    private String mchId;

    /**
     * 商户key
     */
    private String mchKey;

    /**
     * 交易类型
     * JSAPI--公众号支付
     * NATIVE--原生扫码支付
     * APP--app支付
     */
    private String tradeType;

    /**
     * 支付成功回调地址
     */
    private String notifyUrl;

    /**
     * 用户openId
     */
    private String openId;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getMchKey() {
        return mchKey;
    }

    public void setMchKey(String mchKey) {
        this.mchKey = mchKey;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
