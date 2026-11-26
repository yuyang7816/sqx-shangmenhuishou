package com.sqx.app.payment.record.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户支付记录表
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-08-13 15:19:30
 */
@Getter
@Setter
@TableName("user_payment_record")
@Schema(name = "UserPaymentRecord", description = "用户支付记录表")
public class UserPaymentRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(description = "支付记录ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户ID")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "业务订单号（如会员订单、商品订单）")
    @TableField("order_no")
    private String orderNo;

    @Schema(description = "支付单号（系统生成，全局唯一）")
    @TableField("payment_no")
    private String paymentNo;

    @Schema(description = "第三方支付流水号（微信/支付宝等）")
    @TableField("trade_no")
    private String tradeNo;

    @Schema(description = "支付金额（单位：元）")
    @TableField("amount")
    private BigDecimal amount;

    @Schema(description = "支付渠道: wx=微信, ali=支付宝, paypal=PayPal, stripe=Stripe, balance=余额支付")
    @TableField("payment_channel")
    private String paymentChannel;

    @Schema(description = "支付方式: APP, WAP, JSAPI, NATIVE, QR_CODE 等")
    @TableField("payment_method")
    private String paymentMethod;

    @Schema(description = "业务类型: 1=缴纳保证金")
    @TableField("biz_type")
    private Integer bizType;

    @Schema(description = "支付状态: 0=待支付, 1=支付成功, 2=已关闭, 3=支付失败, 4=退款中, 5=已退款")
    @TableField("status")
    private Integer status;

    @Schema(description = "客户端类型: iOS, Android, Web, MiniProgram, PC")
    @TableField("client_type")
    private String clientType;

    @Schema(description = "支付发起IP（支持IPv6）")
    @TableField("ip_address")
    private String ipAddress;

    @Schema(description = "支付成功时间")
    @TableField("pay_time")
    private LocalDateTime payTime;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    @Schema(description = "支付回调通知次数")
    @TableField("notify_count")
    private Integer notifyCount;

    @Schema(description = "最后一次回调时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("last_notify_time")
    private LocalDateTime lastNotifyTime;

    @Schema(description = "扩展字段（JSON格式，如设备信息、支付参数）")
    @TableField("extra")
    private String extra;

}
