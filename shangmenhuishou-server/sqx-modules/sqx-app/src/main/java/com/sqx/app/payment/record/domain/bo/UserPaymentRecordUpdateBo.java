package com.sqx.app.payment.record.domain.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-08-13 15:19:30
 */
@Data
public class UserPaymentRecordUpdateBo {

    @Schema(description = "支付记录ID")
    private Long id;
    @Schema(description = "用户ID")
    private Long userId;
    @Schema(description = "业务订单号（如会员订单、商品订单）")
    private String orderNo;
    @Schema(description = "支付单号（系统生成，全局唯一）")
    private String paymentNo;
    @Schema(description = "第三方支付流水号（微信/支付宝等）")
    private String tradeNo;
    @Schema(description = "支付金额（单位：元）")
    private BigDecimal amount;
    @Schema(description = "支付渠道: 1=微信, 2=支付宝, 3=PayPal, 4=Stripe, 5=余额支付")
    private Byte paymentChannel;
    @Schema(description = "支付方式: APP, WAP, JSAPI, NATIVE, QR_CODE 等")
    private String paymentMethod;
    @Schema(description = "业务类型: 1=会员购买, 2=订单支付, 3=充值, 4=活动缴费等")
    private Byte bizType;
    @Schema(description = "支付状态: 0=待支付, 1=支付成功, 2=已关闭, 3=支付失败, 4=退款中, 5=已退款")
    private Byte status;
    @Schema(description = "客户端类型: iOS, Android, Web, MiniProgram, PC")
    private String clientType;
    @Schema(description = "支付发起IP（支持IPv6）")
    private String ipAddress;
    @Schema(description = "支付成功时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payTime;
    @Schema(description = "支付回调通知次数")
    private Integer notifyCount;
    @Schema(description = "最后一次回调时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastNotifyTime;
    @Schema(description = "扩展字段（JSON格式，如设备信息、支付参数）")
    private String extra;
}
