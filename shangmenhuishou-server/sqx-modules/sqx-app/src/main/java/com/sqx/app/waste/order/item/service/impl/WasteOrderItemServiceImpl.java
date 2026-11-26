package com.sqx.app.waste.order.item.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.app.waste.order.item.domain.vo.WasteOrderItemVo;
import com.sqx.app.waste.order.item.db.service.IWasteOrderItemService;
import com.sqx.app.waste.order.item.domain.bo.WasteOrderItemInfoBo;
import com.sqx.app.waste.order.item.domain.bo.WasteOrderItemPageBo;
import com.sqx.app.waste.order.item.domain.bo.WasteOrderItemSaveBo;
import com.sqx.app.waste.order.item.domain.bo.WasteOrderItemUpdateBo;
import com.sqx.app.waste.order.item.domain.entity.WasteOrderItem;
import com.sqx.app.waste.order.item.service.WasteOrderItemService;
import com.sqx.common.core.exception.BizException;
import com.sqx.common.mybatis.utils.PageUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 订单关联的项目 服务实现类
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-03 14:28:51
 */
@Service
public class WasteOrderItemServiceImpl implements WasteOrderItemService {

    @Resource
    private IWasteOrderItemService iWasteOrderItemService;

    @Override
    public void saveInfo(WasteOrderItemSaveBo wasteOrderItemSaveBo) {
        WasteOrderItem wasteOrderItem = new WasteOrderItem();
        BeanUtils.copyProperties(wasteOrderItemSaveBo, wasteOrderItem);

        iWasteOrderItemService.save(wasteOrderItem);
    }

    @Override
    public IPage<WasteOrderItemVo> pageInfo(WasteOrderItemPageBo wasteOrderItemPageBo) {
        Page<WasteOrderItem> initPage = PageUtil.initPage(wasteOrderItemPageBo.getPage(), wasteOrderItemPageBo.getLimit());

        QueryWrapper<WasteOrderItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda();

        IPage<WasteOrderItem> iPage = iWasteOrderItemService.page(initPage, queryWrapper);
        List<WasteOrderItemVo> voList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
            for (WasteOrderItem record : iPage.getRecords()) {
                WasteOrderItemVo vo = new WasteOrderItemVo();
                BeanUtils.copyProperties(record, vo);

                voList.add(vo);
            }
        }

        IPage<WasteOrderItemVo> voIPage = PageUtil.copyPage(iPage, voList);
        return voIPage;
    }

    @Override
    public WasteOrderItemVo info(WasteOrderItemInfoBo wasteOrderItemInfoBo) {
        WasteOrderItem wasteOrderItem = iWasteOrderItemService.getById(wasteOrderItemInfoBo.getId());
        if (Objects.isNull(wasteOrderItem)) {
            throw new BizException("信息不存在");
        }

        WasteOrderItemVo vo = new WasteOrderItemVo();
        BeanUtils.copyProperties(wasteOrderItem, vo);
        return vo;
    }

    @Override
    public void updateInfo(WasteOrderItemUpdateBo wasteOrderItemUpdateBo) {
        WasteOrderItem wasteOrderItem = new WasteOrderItem();
        BeanUtils.copyProperties(wasteOrderItemUpdateBo, wasteOrderItem);

        iWasteOrderItemService.updateById(wasteOrderItem);
    }

    @Override
    public void delete(WasteOrderItemInfoBo wasteOrderItemInfoBo) {
        iWasteOrderItemService.removeById(wasteOrderItemInfoBo.getId());
    }
}
