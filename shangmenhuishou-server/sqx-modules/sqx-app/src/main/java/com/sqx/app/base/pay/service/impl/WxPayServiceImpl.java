package com.sqx.app.base.pay.service.impl;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.impl.WxPayServiceApacheHttpImpl;
import com.sqx.app.base.pay.domain.BusinessParam;
import com.sqx.app.base.pay.domain.WxPayParam;
import com.sqx.app.base.pay.enums.PayCommonInfoCode;
import com.sqx.app.base.pay.enums.PlatformEnum;
import com.sqx.app.base.pay.enums.WxTradeType;
import com.sqx.app.base.pay.business.service.BusinessService;
import com.sqx.app.base.pay.business.service.CallbackService;
import com.sqx.app.base.pay.service.WxPayService;
import com.sqx.app.base.pay.utils.PayUtil;
import com.sqx.common.core.domain.Result;
import com.sqx.common.core.exception.BizException;
import com.sqx.common.core.utils.GenUtil;
import com.sqx.common.pay.platform.wechat.WechatPay;
import com.sqx.common.pay.platform.wechat.domain.WxPayConfigParam;
import com.sqx.domain.db.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author chengming
 * @since 2025/4/22
 */
@Slf4j
@Service
public class WxPayServiceImpl implements WxPayService {

    /**
     * 微信支付成功回调地址
     */
    public static final String NOTIFY_URL = "/sqx_fast/app/wxpay/notify_url";

    @Autowired
    private BusinessService businessService;
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private CallbackService callbackService;

    @Override
    public <T> Result<T> pay(BusinessParam param) {
        // 订单号
        String orderNo = GenUtil.genOrderNo();
        String subject = "订单标题";

        // 处理业务
        param.setOrderNo(orderNo);
        param.setPlatform(PlatformEnum.WX.getCode());
        businessService.process(param);

        WxPayParam wxPayParam = new WxPayParam();
        wxPayParam.setClientId(param.getClientId());
        wxPayParam.setTradeType(param.getTradeType());
        wxPayParam.setOpenId(param.getOpenId());
        WxPayConfigParam wxPayConfigParam = buildWxPayConfigParam(wxPayParam);
        T res;
        try {
            res = WechatPay.pay(orderNo, String.valueOf(param.getAmount()), subject, wxPayConfigParam);
        } catch (WxPayException e) {
            e.printStackTrace();
            throw new BizException("交易失败");
        }

        return Result.success(res);
    }

    @Override
    public String notifyUrl(HttpServletRequest request) {
        String xml = PayUtil.requestToXml(request);

        try {
            WxPayServiceApacheHttpImpl wxPayServiceApacheHttp = new WxPayServiceApacheHttpImpl();
            WxPayOrderNotifyResult result = wxPayServiceApacheHttp.parseOrderNotifyResult(xml);
            log.info(result.toString());

            callbackService.successProcess(result.getOutTradeNo(), result.getTransactionId());
        } catch (WxPayException e) {
            log.error("微信回调结果异常,异常原因{}", e.getMessage());
            return WxPayNotifyResponse.fail(e.getMessage());
        }

        return WxPayNotifyResponse.success("处理成功");
    }

    private WxPayConfigParam buildWxPayConfigParam(WxPayParam wxPayParam) {
        WxPayConfigParam wxPayConfigParam = new WxPayConfigParam();

        String appId = getWxPayAppId(wxPayParam.getTradeType());
        wxPayConfigParam.setAppId(appId);

        String mchId = sysConfigService.getConfigValue(PayCommonInfoCode.WX_MCH_ID.getCode());
        wxPayConfigParam.setMchId(mchId);

        String mchKey = sysConfigService.getConfigValue(PayCommonInfoCode.WX_MCH_KEY.getCode());
        wxPayConfigParam.setMchKey(mchKey);

        wxPayConfigParam.setTradeType(wxPayParam.getTradeType());
        wxPayConfigParam.setOpenId(wxPayParam.getOpenId());

        String domain = sysConfigService.getConfigValue(wxPayParam.getClientId());
        wxPayConfigParam.setNotifyUrl(domain + NOTIFY_URL);

        return wxPayConfigParam;
    }

    /**
     * 获取微信支付appid
     * @param tradeType 交易类型 JSAPI-公众号支付 NATIVE-原生扫码支付 APP-app支付
     */
    private String getWxPayAppId(String tradeType) {
        String appId;
        if (Objects.equals(tradeType, WxTradeType.JSAPI.getCode())) {
            appId = sysConfigService.getConfigValue(PayCommonInfoCode.WX_JSAPI_APP_ID.getCode());
        } else if (Objects.equals(tradeType, WxTradeType.NATIVE.getCode())) {
            appId = sysConfigService.getConfigValue(PayCommonInfoCode.WX_H5_APP_ID.getCode());
        } else if (Objects.equals(tradeType, WxTradeType.APP.getCode())) {
            appId = sysConfigService.getConfigValue(PayCommonInfoCode.WX_APP_ID.getCode());
        } else {
            log.error("不支持的交易类型: {}", tradeType);
            throw new BizException("不支持的交易类型");
        }

        return appId;
    }
}
