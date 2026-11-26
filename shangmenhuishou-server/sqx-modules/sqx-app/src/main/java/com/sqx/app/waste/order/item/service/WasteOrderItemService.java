package com.sqx.app.waste.order.item.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.waste.order.item.domain.bo.WasteOrderItemInfoBo;
import com.sqx.app.waste.order.item.domain.bo.WasteOrderItemPageBo;
import com.sqx.app.waste.order.item.domain.bo.WasteOrderItemSaveBo;
import com.sqx.app.waste.order.item.domain.bo.WasteOrderItemUpdateBo;
import com.sqx.app.waste.order.item.domain.vo.WasteOrderItemVo;

/**
 * 订单关联的项目 服务类
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-03 14:28:51
 */
public interface WasteOrderItemService {

    void saveInfo(WasteOrderItemSaveBo wasteOrderItemSaveBo);

    IPage<WasteOrderItemVo> pageInfo(WasteOrderItemPageBo wasteOrderItemPageBo);

    WasteOrderItemVo info(WasteOrderItemInfoBo wasteOrderItemInfoBo);

    void updateInfo(WasteOrderItemUpdateBo wasteOrderItemUpdateBo);

    void delete(WasteOrderItemInfoBo wasteOrderItemInfoBo);
}
