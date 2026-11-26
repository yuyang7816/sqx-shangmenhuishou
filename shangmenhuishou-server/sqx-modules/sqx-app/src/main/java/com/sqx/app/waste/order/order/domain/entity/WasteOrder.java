package com.sqx.app.waste.order.order.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 订单
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-03 14:28:34
 */
@Getter
@Setter
@TableName("waste_order")
@Schema(name = "WasteOrder", description = "订单")
public class WasteOrder implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(description = "订单id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户id")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "订单编号")
    @TableField("order_no")
    private String orderNo;

    @Schema(description = "订单状态：0=待接单；1=待服务；2=进行中；3=已完成；4=已取消；")
    @TableField("status")
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
    @TableField("note")
    private String note;

    @Schema(description = "回收员用户id")
    private Long wasteUserId;

    @Schema(description = "接单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime wasteTime;

    @Schema(description = "预约时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("appointment_time")
    private LocalDateTime appointmentTime;

    @Schema(description = "提交时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

}
