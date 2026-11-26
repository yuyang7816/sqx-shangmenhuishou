package com.sqx.app.waste.order.order.domain.bo;

import com.sqx.common.mybatis.domain.PageParam ;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-03 14:28:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WasteOrderPageBo extends PageParam {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "订单编号")
    private String orderNo;

    @Schema(description = "订单状态：0=待接单；1=待服务；2=进行中；3=已完成；4=已取消；")
    private Integer status;

    @Schema(description = "回收员用户id")
    private Long wasteUserId;

    @Schema(description = "用户名称")
    private String userName;

    @Schema(description = "回收员用户名称")
    private String wasteUserName;
}
