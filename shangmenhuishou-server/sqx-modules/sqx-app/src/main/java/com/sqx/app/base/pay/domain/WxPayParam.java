package com.sqx.app.base.pay.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @author chengming
 * @since 2025/4/22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WxPayParam extends BasePayParam {

    @NotBlank(message = "交易类型不能为空")
    @Schema(description = "交易类型 JSAPI-公众号支付 NATIVE-原生扫码支付 APP-app支付")
    private String tradeType;

    @Schema(description = "用户openId")
    private String openId;
}
