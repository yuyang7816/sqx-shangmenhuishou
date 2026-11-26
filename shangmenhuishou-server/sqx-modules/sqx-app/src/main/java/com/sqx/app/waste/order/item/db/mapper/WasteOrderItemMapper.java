package com.sqx.app.waste.order.item.db.mapper;

import com.sqx.app.utils.model.StartAndEndTime;
import com.sqx.app.waste.order.item.domain.entity.WasteOrderItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * <p>
 * 订单关联的项目 Mapper 接口
 * </p>
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-03 14:28:51
 */
public interface WasteOrderItemMapper extends BaseMapper<WasteOrderItem> {

    BigDecimal sumAmount(@Param("param") StartAndEndTime startAndEndTime);
}
