package com.sqx.app.base.pay.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author chengming
 * @since 2025/4/21
 *
 * 业务参数
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AliPayParam extends AliPagePayParam {

}
