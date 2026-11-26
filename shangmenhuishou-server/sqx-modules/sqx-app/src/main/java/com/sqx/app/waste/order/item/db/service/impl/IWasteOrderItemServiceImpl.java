package com.sqx.app.waste.order.item.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.app.utils.model.StartAndEndTime;
import com.sqx.app.waste.order.item.db.mapper.WasteOrderItemMapper;
import com.sqx.app.waste.order.item.db.service.IWasteOrderItemService;
import com.sqx.app.waste.order.item.domain.entity.WasteOrderItem;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 订单关联的项目 服务实现类
 * </p>
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-03 14:28:51
 */
@Service
public class IWasteOrderItemServiceImpl extends ServiceImpl<WasteOrderItemMapper, WasteOrderItem> implements IWasteOrderItemService {

    @Resource
    private WasteOrderItemMapper wasteOrderItemMapper;

    @Override
    public List<WasteOrderItem> listByOrderId(Long orderId) {
        LambdaQueryWrapper<WasteOrderItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WasteOrderItem::getOrderId, orderId);

        return this.list(queryWrapper);
    }

    @Override
    public BigDecimal sumAmount(StartAndEndTime startAndEndTime) {
        return wasteOrderItemMapper.sumAmount(startAndEndTime);
    }
}
