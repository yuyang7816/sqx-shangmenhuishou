package com.sqx.app.waste.price.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.app.waste.price.db.mapper.WastePriceMapper;
import com.sqx.app.waste.price.db.service.IWastePriceService;
import com.sqx.app.waste.price.domain.entity.WastePrice;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 废品价格 服务实现类
 * </p>
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-03 11:37:16
 */
@Service
public class IWastePriceServiceImpl extends ServiceImpl<WastePriceMapper, WastePrice> implements IWastePriceService {

    @Override
    public WastePrice getByCategoryId(Long categoryId) {
        LambdaQueryWrapper<WastePrice> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WastePrice::getCategoryId, categoryId);
        return this.getOne(queryWrapper);
    }
}
