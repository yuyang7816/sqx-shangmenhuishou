package com.sqx.app.waste.order.order.domain.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author chengming
 * @since 2025/10/15
 */
@Data
public class ItemOrderBo {

    @Schema(description = "用户id", hidden = true)
    private Long userId;

    @Schema(description = "地址id")
    @NotNull(message = "请选择地址")
    private Long addressId;

    @Schema(description = "预约时间")
    @NotNull(message = "请选择预约时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime appointmentTime;

    @Schema(description = "回收项目id")
    @NotNull(message = "回收项目id不能为空")
    private Long itemId;

    @Schema(description = "数量")
    @Min(value = 1, message = "数量不能小于1")
    private Integer num;

    @Schema(description = "单位")
    private String unit;

    @Schema(description = "备注")
    private String note;
}
