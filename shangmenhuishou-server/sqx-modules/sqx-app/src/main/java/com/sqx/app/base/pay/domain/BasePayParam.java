package com.sqx.app.base.pay.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author chengming
 * @since 2025/4/21
 * 基础支付参数
 */
@Data
public class BasePayParam {

    @NotNull(message = "客户端id不能为空")
    @Schema(name = "clientId", description = "客户端id")
    private String clientId;
}
