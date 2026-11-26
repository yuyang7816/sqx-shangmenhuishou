package com.sqx.app.waste.order.item.db.service;

import com.sqx.app.utils.model.StartAndEndTime;
import com.sqx.app.waste.order.item.domain.entity.WasteOrderItem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 订单关联的项目 服务类
 * </p>
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-03 14:28:51
 */
public interface IWasteOrderItemService extends IService<WasteOrderItem> {

    List<WasteOrderItem> listByOrderId(Long orderId);

    BigDecimal sumAmount(StartAndEndTime startAndEndTime);
}
