package com.sqx.app.waste.order.order.db.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.app.waste.order.order.domain.bo.WasteOrderPageBo;
import com.sqx.app.waste.order.order.domain.entity.WasteOrder;
import com.sqx.app.waste.order.order.domain.vo.WasteOrderVo;

import java.math.BigDecimal;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-03 14:28:34
 */
public interface IWasteOrderService extends IService<WasteOrder> {

    WasteOrderVo information(Long id, BigDecimal lat, BigDecimal lng);

    IPage<WasteOrderVo> pageInfo(Page<WasteOrder> initPage, WasteOrderPageBo wasteOrderPageBo);
}
