package com.sqx.app.waste.order.order.domain.vo;

import com.sqx.app.waste.order.item.domain.entity.WasteOrderItem;
import com.sqx.app.waste.order.order.domain.entity.WasteOrder;
import com.sqx.domain.db.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-03 14:28:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WasteOrderVo extends WasteOrder{

    @Schema(description = "距离")
    private BigDecimal distance;

    @Schema(description = "预约物品列表")
    private List<WasteOrderItem> orderItems;

    @Schema(description = "接单人")
    private UserEntity wasteUser;

    @Schema(description = "发单人")
    private UserEntity userInfo;

    @Schema(description = "用户名称")
    private String userName;

    @Schema(description = "接单人名称")
    private String wasteUserName;
}
