package com.sqx.app.waste.price.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.app.waste.price.db.service.IWastePriceService;
import com.sqx.app.waste.price.domain.bo.*;
import com.sqx.app.waste.price.domain.entity.WastePrice;
import com.sqx.app.waste.price.domain.vo.WastePriceVo;
import com.sqx.app.waste.price.service.WastePriceService;
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
 * 废品价格 服务实现类
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-03 11:37:16
 */
@Service
public class WastePriceServiceImpl implements WastePriceService {

    @Resource
    private IWastePriceService iWastePriceService;

    @Override
    public void saveInfo(WastePriceSaveBo wastePriceSaveBo) {
        WastePrice wastePrice = new WastePrice();
        BeanUtils.copyProperties(wastePriceSaveBo, wastePrice);

        iWastePriceService.save(wastePrice);
    }

    @Override
    public IPage<WastePriceVo> pageInfo(WastePricePageBo wastePricePageBo) {
        Page<WastePrice> initPage = PageUtil.initPage(wastePricePageBo.getPage(), wastePricePageBo.getLimit());

        QueryWrapper<WastePrice> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(WastePrice::getCategoryId, wastePricePageBo.getCategoryId())
                .orderByDesc(WastePrice::getCreateTime);

        IPage<WastePrice> iPage = iWastePriceService.page(initPage, queryWrapper);
        List<WastePriceVo> voList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
            for (WastePrice record : iPage.getRecords()) {
                WastePriceVo vo = new WastePriceVo();
                BeanUtils.copyProperties(record, vo);

                voList.add(vo);
            }
        }

        IPage<WastePriceVo> voIPage = PageUtil.copyPage(iPage, voList);
        return voIPage;
    }

    @Override
    public WastePriceVo info(WastePriceInfoBo wastePriceInfoBo) {
        WastePrice wastePrice = iWastePriceService.getById(wastePriceInfoBo.getId());
        if (Objects.isNull(wastePrice)) {
            throw new BizException("信息不存在");
        }

        WastePriceVo vo = new WastePriceVo();
        BeanUtils.copyProperties(wastePrice, vo);
        return vo;
    }

    @Override
    public void updateInfo(WastePriceUpdateBo wastePriceUpdateBo) {
        WastePrice wastePrice = new WastePrice();
        BeanUtils.copyProperties(wastePriceUpdateBo, wastePrice);

        iWastePriceService.updateById(wastePrice);
    }

    @Override
    public void delete(WastePriceInfoBo wastePriceInfoBo) {
        iWastePriceService.removeById(wastePriceInfoBo.getId());
    }

    @Override
    public List<WastePriceVo> listInfo(WastePriceListBo wastePriceListBo) {
        QueryWrapper<WastePrice> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(WastePrice::getCategoryId, wastePriceListBo.getCategoryId())
                .orderByDesc(WastePrice::getCreateTime);

        List<WastePrice> wastePriceList = iWastePriceService.list(queryWrapper);
        List<WastePriceVo> voList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(wastePriceList)) {
            for (WastePrice record : wastePriceList) {
                WastePriceVo vo = new WastePriceVo();
                BeanUtils.copyProperties(record, vo);

                voList.add(vo);
            }
        }

        return voList;
    }
}
