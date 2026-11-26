package com.sqx.app.waste.order.order.db.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.app.waste.order.order.domain.bo.WasteOrderPageBo;
import com.sqx.app.waste.order.order.domain.entity.WasteOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.app.waste.order.order.domain.vo.WasteOrderVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * <p>
 * 订单 Mapper 接口
 * </p>
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-03 14:28:34
 */
public interface WasteOrderMapper extends BaseMapper<WasteOrder> {

    WasteOrderVo information(@Param("id") Long id, @Param("lat") BigDecimal lat, @Param("lng") BigDecimal lng);

    IPage<WasteOrderVo> pageInfo(Page<WasteOrder> initPage, @Param("param") WasteOrderPageBo wasteOrderPageBo);
}
