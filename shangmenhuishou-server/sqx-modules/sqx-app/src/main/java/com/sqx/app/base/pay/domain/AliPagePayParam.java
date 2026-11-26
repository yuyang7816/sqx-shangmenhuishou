package com.sqx.app.base.pay.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author chengming
 * @since 2025/4/21
 * 支付宝电脑网页支付参数
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AliPagePayParam extends BasePayParam {

    @Schema(name = "qrPayMode", description = "PC扫码支付的方式 " +
            "支持前置模式和跳转模式" +
            "前置模式是将二维码前置到商户的订单确认页的模式。需要商户在自己的页面中以 iframe 方式请求支付宝页面。具体支持的枚举值有以下几种：" +
            "0：订单码-简约前置模式，对应 iframe 宽度不能小于600px，高度不能小于300px；" +
            "1：订单码-前置模式，对应iframe 宽度不能小于 300px，高度不能小于600px；" +
            "3：订单码-迷你前置模式，对应 iframe 宽度不能小于 75px，高度不能小于75px；" +
            "4：订单码-可定义宽度的嵌入式二维码，商户可根据需要设定二维码的大小。" +
            "跳转模式下，用户的扫码界面是由支付宝生成的，不在商户的域名下。支持传入的枚举值有：" +
            "2：订单码-跳转模式")
    private String qrPayMode;

    @Schema(name = "qrcodeWidth", description = "商户自定义二维码宽度 qr_pay_mode=4时该参数有效")
    private Long qrcodeWidth;
}
