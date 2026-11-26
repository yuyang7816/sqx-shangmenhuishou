package com.sqx.app.base.pay.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConstants;
import com.alipay.api.internal.util.AlipaySignature;
import com.sqx.app.base.pay.domain.BusinessParam;
import com.sqx.app.base.pay.enums.PlatformEnum;
import com.sqx.common.core.domain.Result;
import com.sqx.common.core.exception.SqxException;
import com.sqx.common.core.utils.GenUtil;
import com.sqx.common.pay.platform.ali.AliPay;
import com.sqx.common.pay.platform.ali.domain.AlipayConfigParam;
import com.sqx.app.base.pay.domain.AliPayParam;
import com.sqx.app.base.pay.enums.AliMode;
import com.sqx.app.base.pay.enums.AliSandbox;
import com.sqx.app.base.pay.enums.PayCommonInfoCode;
import com.sqx.app.base.pay.service.AlipayService;
import com.sqx.app.base.pay.business.service.BusinessService;
import com.sqx.app.base.pay.business.service.CallbackService;
import com.sqx.app.base.pay.utils.PayUtil;
import com.sqx.domain.db.service.ISysConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

/**
 * @author chengming
 * @since 2025/4/21
 */
@Service
public class AlipayServiceImpl implements AlipayService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlipayServiceImpl.class);

    public static final String NOTIFY_URL = "/sqx_fast/app/alipay/notifyUrl";
    public static final String CERT_NOTIFY_URL = "/sqx_fast/app/alipay/certNotifyUrl";

    public static final String RETURN_URL = "/sqx_fast/app/alipay/returnUrl";
    public static final String CERT_RETURN_URL = "/sqx_fast/app/alipay/certReturnUrl";

    @Autowired
    private CallbackService callbackService;
    @Autowired
    private BusinessService businessService;
    @Autowired
    private ISysConfigService sysConfigService;

    @Override
    public Result<String> wapPay(BusinessParam param) {
        // 订单号
        String orderNo = GenUtil.genOrderNo();

        // 处理业务
        param.setOrderNo(orderNo);
        param.setPlatform(PlatformEnum.ALI.getCode());
        businessService.process(param);

        // 请求支付
        AlipayConfigParam alipayConfigParam = buildAlipayConfigParam();

        String domain = sysConfigService.getConfigValue(param.getClientId());

        String notifyUrl = domain + NOTIFY_URL;
        String returnUrl = domain + RETURN_URL;

        String mode = sysConfigService.getConfigValue(PayCommonInfoCode.ALI_MODE.getCode());
        if (Objects.equals(mode, AliMode.CERT.getCode())) {
            notifyUrl = domain + CERT_NOTIFY_URL;
            returnUrl = domain + CERT_RETURN_URL;
        }

        String subject = "订单标题";

        try {
            String res = AliPay.wapPay(orderNo, String.valueOf(param.getAmount()), subject, returnUrl, notifyUrl, alipayConfigParam);

            return Result.success(res);
        } catch (AlipayApiException e) {
            throw new SqxException(e.getMessage());
        }
    }

    @Override
    public Result<String> appPay(BusinessParam param) {
        // 订单号
        String orderNo = GenUtil.genOrderNo();

        // 处理业务
        param.setOrderNo(orderNo);
        param.setPlatform(PlatformEnum.ALI.getCode());
        businessService.process(param);

        // 请求支付
        AlipayConfigParam alipayConfigParam = buildAlipayConfigParam();

        String domain = sysConfigService.getConfigValue(param.getClientId());
        String notifyUrl = domain + NOTIFY_URL;
        String returnUrl = domain + RETURN_URL;

        String mode = sysConfigService.getConfigValue(PayCommonInfoCode.ALI_MODE.getCode());
        if (Objects.equals(mode, AliMode.CERT.getCode())) {
            notifyUrl = domain + CERT_NOTIFY_URL;
            returnUrl = domain + CERT_RETURN_URL;
        }

        String subject = "订单标题";

        try {
            String res = AliPay.appPay(orderNo, String.valueOf(param.getAmount()), subject, returnUrl, notifyUrl, alipayConfigParam);

            return Result.success(res);
        } catch (AlipayApiException e) {
            throw new SqxException(e.getMessage());
        }
    }

    @Override
    public Result<String> pagePay(BusinessParam param) {
        // 订单号
        String orderNo = GenUtil.genOrderNo();

        // 处理业务
        param.setOrderNo(orderNo);
        param.setPlatform(PlatformEnum.ALI.getCode());
        businessService.process(param);

        // 请求支付
        AlipayConfigParam alipayConfigParam = buildAlipayConfigParam();

        String domain = sysConfigService.getConfigValue(param.getClientId());
        String notifyUrl = domain + NOTIFY_URL;
        String returnUrl = domain + RETURN_URL;

        String mode = sysConfigService.getConfigValue(PayCommonInfoCode.ALI_MODE.getCode());
        if (Objects.equals(mode, AliMode.CERT.getCode())) {
            notifyUrl = domain + CERT_NOTIFY_URL;
            returnUrl = domain + CERT_RETURN_URL;
        }

        String subject = "订单标题";

        try {
            String res = AliPay.pagePay(orderNo, String.valueOf(param.getAmount()), subject, returnUrl, notifyUrl, "1", 100L ,alipayConfigParam);

            return Result.success(res);
        } catch (AlipayApiException e) {
            throw new SqxException(e.getMessage());
        }
    }

    @Override
    public String notifyUrl(HttpServletRequest request) {
        LOGGER.info("支付宝普通公钥模式同步通知");

        Map<String, String> params = PayUtil.requestToMap(request);
        params.forEach((key, value) -> LOGGER.info("{} = {}", key, value));

        String publicKey = sysConfigService.getConfigValue(PayCommonInfoCode.ALI_PUBLIC_KEY.getCode());
        try {
            boolean verifyResult = AlipaySignature.rsaCheckV1(params, publicKey, AlipayConstants.CHARSET_UTF8, AlipayConstants.SIGN_TYPE_RSA2);
            if (!verifyResult) {
                LOGGER.error("支付宝回调验证失败");
                return "failed";
            }

            // 支付成功业务
            callbackService.successProcess(params.get("out_trade_no"), params.get("trade_no"));

            return "success";
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "failed";
        }
    }

    @Override
    public String certNotifyUrl(HttpServletRequest request) {
        LOGGER.info("支付宝证书模式同步通知");

        Map<String, String> params = PayUtil.requestToMap(request);
        params.forEach((key, value) -> LOGGER.info("{} = {}", key, value));

        String publicCertPath = sysConfigService.getConfigValue(PayCommonInfoCode.ALI_ALIPAY_PUBLIC_CERT_PATH.getCode());
        try {
            boolean verifyResult = AlipaySignature.rsaCertCheckV1(params, publicCertPath, AlipayConstants.CHARSET_UTF8, AlipayConstants.SIGN_TYPE_RSA2);
            if (!verifyResult) {
                LOGGER.error("支付宝回调验证失败");
                return "failed";
            }

            // 支付成功业务
            callbackService.successProcess(params.get("out_trade_no"), params.get("trade_no"));

            return "success";
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "failed";
        }
    }

    @Override
    public String returnUrl(HttpServletRequest request) {
        LOGGER.info("支付宝普通公钥模式异步通知");

        Map<String, String> params = PayUtil.requestToMap(request);
        params.forEach((key, value) -> LOGGER.info("{} = {}", key, value));

        String publicKey = sysConfigService.getConfigValue(PayCommonInfoCode.ALI_PUBLIC_KEY.getCode());
        try {
            boolean verifyResult = AlipaySignature.rsaCheckV1(params, publicKey, AlipayConstants.CHARSET_UTF8, AlipayConstants.SIGN_TYPE_RSA2);
            if (!verifyResult) {
                LOGGER.error("支付宝回调验证失败");
                return "failed";
            }

            // 支付成功业务
            callbackService.successProcess(params.get("out_trade_no"), params.get("trade_no"));

            return "success";
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "failed";
        }
    }

    @Override
    public String certReturnUrl(HttpServletRequest request) {
        LOGGER.info("支付宝证书模式异步通知");

        Map<String, String> params = PayUtil.requestToMap(request);
        params.forEach((key, value) -> LOGGER.info("{} = {}", key, value));

        String publicCertPath = sysConfigService.getConfigValue(PayCommonInfoCode.ALI_ALIPAY_PUBLIC_CERT_PATH.getCode());
        try {
            boolean verifyResult = AlipaySignature.rsaCertCheckV1(params, publicCertPath, AlipayConstants.CHARSET_UTF8, AlipayConstants.SIGN_TYPE_RSA2);
            if (!verifyResult) {
                LOGGER.error("支付宝回调验证失败");
                return "failed";
            }

            // 支付成功业务
            callbackService.successProcess(params.get("out_trade_no"), params.get("trade_no"));

            return "success";
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "failed";
        }
    }

    /**
     * 支付宝支付配置
     */
    private AlipayConfigParam buildAlipayConfigParam() {
        AlipayConfigParam alipayConfigParam = new AlipayConfigParam();
        String appId = sysConfigService.getConfigValue(PayCommonInfoCode.ALI_APPID.getCode());
        alipayConfigParam.setAppId(appId);
        String privateKey = sysConfigService.getConfigValue(PayCommonInfoCode.ALI_PRIVATE_KEY.getCode());
        alipayConfigParam.setPrivateKey(privateKey);

        // 是否为沙箱模式
        String sandbox = sysConfigService.getConfigValue(PayCommonInfoCode.ALI_SANDBOX.getCode());
        if (Objects.equals(sandbox, AliSandbox.YES.getCode())) {
            alipayConfigParam.setSandbox(Boolean.TRUE);
        }

        // 是否开启证书模式
        String aliMode = sysConfigService.getConfigValue(PayCommonInfoCode.ALI_MODE.getCode());
        if (Objects.equals(AliMode.PUBLIC_KEY.getCode(), aliMode)) {
            String publicKey = sysConfigService.getConfigValue(PayCommonInfoCode.ALI_PUBLIC_KEY.getCode());
            alipayConfigParam.setAlipayPublicKey(publicKey);

            alipayConfigParam.setCertMode(Boolean.FALSE);
        } else if (Objects.equals(AliMode.CERT.getCode(), aliMode)) {
            String appCertPath = sysConfigService.getConfigValue(PayCommonInfoCode.ALI_APP_CERT_PATH.getCode());
            alipayConfigParam.setAppCertPath(appCertPath);

            String alipayPublicCertPath = sysConfigService.getConfigValue(PayCommonInfoCode.ALI_ALIPAY_PUBLIC_CERT_PATH.getCode());
            alipayConfigParam.setAlipayPublicCertPath(alipayPublicCertPath);

            String rootCertPath = sysConfigService.getConfigValue(PayCommonInfoCode.ALI_ROOT_CERT_PATH.getCode());
            alipayConfigParam.setRootCertPath(rootCertPath);

            alipayConfigParam.setCertMode(Boolean.TRUE);
        } else {
            LOGGER.error("支付宝支付模式配置错误, 当前配置: {}", aliMode);
            throw new SqxException("支付失败");
        }

        return alipayConfigParam;
    }
}
