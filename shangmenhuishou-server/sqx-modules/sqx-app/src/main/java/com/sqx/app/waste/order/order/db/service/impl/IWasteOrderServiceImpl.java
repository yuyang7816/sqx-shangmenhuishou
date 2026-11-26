package com.sqx.app.waste.order.order.db.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.app.waste.order.order.domain.bo.WasteOrderPageBo;
import com.sqx.app.waste.order.order.domain.entity.WasteOrder;
import com.sqx.app.waste.order.order.db.mapper.WasteOrderMapper;
import com.sqx.app.waste.order.order.db.service.IWasteOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.app.waste.order.order.domain.vo.WasteOrderVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-03 14:28:34
 */
@Service
public class IWasteOrderServiceImpl extends ServiceImpl<WasteOrderMapper, WasteOrder> implements IWasteOrderService {

    @Resource
    private WasteOrderMapper wasteOrderMapper;

    @Override
    public WasteOrderVo information(Long id, BigDecimal lat, BigDecimal lng) {
        return wasteOrderMapper.information(id, lat, lng);
    }

    @Override
    public IPage<WasteOrderVo> pageInfo(Page<WasteOrder> initPage, WasteOrderPageBo wasteOrderPageBo) {
        return wasteOrderMapper.pageInfo(initPage, wasteOrderPageBo);
    }
}
