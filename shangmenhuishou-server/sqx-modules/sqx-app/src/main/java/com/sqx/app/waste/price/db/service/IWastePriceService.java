package com.sqx.app.waste.price.db.service;

import com.sqx.app.waste.price.domain.entity.WastePrice;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 废品价格 服务类
 * </p>
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-03 11:37:16
 */
public interface IWastePriceService extends IService<WastePrice> {

    WastePrice getByCategoryId(Long categoryId);
}
