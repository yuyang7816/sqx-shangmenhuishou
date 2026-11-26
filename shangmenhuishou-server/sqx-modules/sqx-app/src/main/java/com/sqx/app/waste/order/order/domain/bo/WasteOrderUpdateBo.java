package com.sqx.app.waste.order.order.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-03 14:28:34
 */
@Data
public class WasteOrderUpdateBo {

    @Schema(description = "订单id")
    private Long id;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "订单编号")
    private String orderNo;

    @Schema(description = "订单状态：0=待接单；1=待服务；2=进行中；3=已完成；4=已取消；")
    private Integer status;

    @Schema(description = "地址姓名")
    private String addressName;

    @Schema(description = "地址电话")
    private String addressPhone;

    @Schema(description = "省")
    private String province;

    @Schema(description = "市")
    private String city;

    @Schema(description = "区")
    private String district;

    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "订单备注")
    private String note;

    @Schema(description = "预约时间")
    private LocalDateTime appointmentTime;
}
