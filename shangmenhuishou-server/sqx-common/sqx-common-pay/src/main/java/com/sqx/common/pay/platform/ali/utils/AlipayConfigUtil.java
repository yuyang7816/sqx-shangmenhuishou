package com.sqx.common.pay.platform.ali.utils;

import com.alipay.api.AlipayConfig;
import com.alipay.api.AlipayConstants;
import com.sqx.common.pay.platform.ali.constants.AlipayConstant;
import com.sqx.common.pay.platform.ali.domain.AlipayConfigParam;
import org.apache.commons.lang3.StringUtils;

/**
 * @author chengming
 * @since 2025/4/18
 */
public class AlipayConfigUtil {

    /**
     * 获取支付宝配置
     * @param param 支付配置参数
     * @return
     */
    public static AlipayConfig getAlipayConfig(AlipayConfigParam param) {
        AlipayConfig alipayConfig = null;

        if (param.getCertMode()) {
            checkCertParam(param);
            alipayConfig = buildCertificateAlipayConfig(param);
        } else {
            checkPublicKeyParam(param);
            alipayConfig = buildPublicKeyAlipayConfig(param);
        }

        // 沙箱模式
        if (param.getSandbox()) {
            alipayConfig.setServerUrl(AlipayConstant.SANDBOX_SERVER_URL);
        }

        return alipayConfig;
    }

    private static void checkCertParam(AlipayConfigParam param) {
        if (StringUtils.isBlank(param.getAppCertPath())) {
            throw new IllegalArgumentException("appCertPath不能为空");
        }

        if (StringUtils.isBlank(param.getAlipayPublicCertPath())) {
            throw new IllegalArgumentException("alipayPublicCertPath不能为空");
        }

        if (StringUtils.isBlank(param.getRootCertPath())) {
            throw new IllegalArgumentException("rootCertPath不能为空");
        }
    }

    private static void checkPublicKeyParam(AlipayConfigParam param) {
        if (StringUtils.isBlank(param.getAlipayPublicKey())) {
            throw new IllegalArgumentException("alipayPublicKey不能为空");
        }
    }

    private static AlipayConfig buildCertificateAlipayConfig(AlipayConfigParam param) {
        AlipayConfig alipayConfig = buildBaseAlipayConfig(param);

        //设置应用公钥证书路径
        alipayConfig.setAppCertPath(param.getAppCertPath());
        //设置支付宝公钥证书路径
        alipayConfig.setAlipayPublicCertPath(param.getAlipayPublicCertPath());
        //设置支付宝根证书路径
        alipayConfig.setRootCertPath(param.getRootCertPath());

        return alipayConfig;
    }

    private static AlipayConfig buildPublicKeyAlipayConfig(AlipayConfigParam param) {
        AlipayConfig alipayConfig = buildBaseAlipayConfig(param);
        //设置支付宝公钥
        alipayConfig.setAlipayPublicKey(param.getAlipayPublicKey());

        return alipayConfig;
    }

    private static AlipayConfig buildBaseAlipayConfig(AlipayConfigParam param) {
        AlipayConfig alipayConfig = new AlipayConfig();
        //设置网关地址
        alipayConfig.setServerUrl(StringUtils.isBlank(param.getServerUrl()) ? AlipayConstant.SERVER_URL : param.getServerUrl());
        //设置应用ID
        alipayConfig.setAppId(param.getAppId());
        //设置应用私钥
        alipayConfig.setPrivateKey(param.getPrivateKey());
        //设置请求格式，固定值json
        alipayConfig.setFormat(StringUtils.isBlank(param.getFormat()) ? AlipayConstants.FORMAT_JSON : param.getFormat());
        //设置字符集
        alipayConfig.setCharset(StringUtils.isBlank(param.getCharset()) ? AlipayConstants.CHARSET_UTF8 : param.getCharset());
        //设置签名类型
        alipayConfig.setSignType(StringUtils.isBlank(param.getSignType()) ? AlipayConstants.SIGN_TYPE_RSA2 : param.getSignType());

        return alipayConfig;
    }
}
