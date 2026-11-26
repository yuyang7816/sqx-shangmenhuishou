package com.sqx.app.waste.order.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.waste.order.order.domain.bo.*;
import com.sqx.app.waste.order.order.domain.vo.TakeOrderDataVo;
import com.sqx.app.waste.order.order.domain.vo.WasteOrderDataVo;
import com.sqx.app.waste.order.order.domain.vo.WasteOrderMyVo;
import com.sqx.app.waste.order.order.domain.vo.WasteOrderVo;

import java.util.List;

/**
 * 订单 服务类
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-03 14:28:34
 */
public interface WasteOrderService {

    void saveInfo(WasteOrderSaveBo wasteOrderSaveBo);

    IPage<WasteOrderVo> pageInfo(WasteOrderPageBo wasteOrderPageBo);

    WasteOrderVo info(WasteOrderInfoBo wasteOrderInfoBo);

    void updateInfo(WasteOrderUpdateBo wasteOrderUpdateBo);

    void delete(WasteOrderInfoBo wasteOrderInfoBo);

    void take(WasteOrderTakeBo wasteOrderTakeBo);

    List<WasteOrderMyVo> myOrder();

    TakeOrderDataVo takeOrderData();

    List<WasteOrderDataVo> wasteOrderData();

    void cancel(WasteOrderCancelBo wasteOrderCancelBo);

    IPage<WasteOrderVo> pool(Long page, Long limit, String city);

    WasteOrderVo information(WasteOrderInformationBo wasteOrderInformationBo);

    void complete(WasteOrderCompleteBo wasteOrderCompleteBo);

    void start(WasteOrderStartBo wasteOrderStartBo);

    void stop(WasteOrderStopBo wasteOrderStopBo);

    void itemOrder(ItemOrderBo itemOrderBo);

    void wasteCancel(WasteOrderCancelBo wasteOrderCancelBo);
}
