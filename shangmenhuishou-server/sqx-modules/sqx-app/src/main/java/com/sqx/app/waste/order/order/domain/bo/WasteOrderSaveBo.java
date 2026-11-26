package com.sqx.app.waste.order.order.domain.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sqx.app.waste.order.item.domain.entity.WasteOrderItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-03 14:28:34
 */
@Data
public class WasteOrderSaveBo {

    @Schema(description = "用户id")
    private Long userId;

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
    @NotNull(message = "请选择预约时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime appointmentTime;

    @Schema(description = "预约物品列表")
    private List<WasteOrderItem> orderItems;

}
