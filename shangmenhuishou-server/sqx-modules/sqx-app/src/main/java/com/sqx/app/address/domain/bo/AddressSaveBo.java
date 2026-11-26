package com.sqx.app.address.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-17 13:54:22
 */
@Data
public class AddressSaveBo {

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "省")
    private String province;

    @Schema(description = "市")
    private String city;

    @Schema(description = "区")
    private String district;

    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "是否默认 1=默认 2=非默认")
    private Integer isDefault;

    @Schema(description = "纬度")
    private BigDecimal lat;

    @Schema(description = "经度")
    private BigDecimal lng;
}
