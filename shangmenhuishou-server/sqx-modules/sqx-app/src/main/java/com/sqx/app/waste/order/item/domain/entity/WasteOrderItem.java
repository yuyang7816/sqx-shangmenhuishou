package com.sqx.app.waste.order.item.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 订单关联的项目
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-03 14:28:51
 */
@Getter
@Setter
@TableName("waste_order_item")
@Schema(name = "WasteOrderItem", description = "订单关联的项目")
public class WasteOrderItem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "订单id")
    @TableField("order_id")
    private Long orderId;

    @Schema(description = "废品类别id")
    @TableField("category_id")
    private Long categoryId;

    @Schema(description = "废品类别名称")
    @TableField("category_name")
    private String categoryName;

    @Schema(description = "废品子类id")
    @TableField("category_children_id")
    private Long categoryChildrenId;

    @Schema(description = "废品子类名称")
    @TableField("category_children_name")
    private String categoryChildrenName;

    @Schema(description = "单位")
    @TableField("unit")
    private String unit;

    @Schema(description = "数量")
    @TableField("num")
    private Integer num;

    @Schema(description = "预估价格")
    @TableField("price")
    private String price;

}
