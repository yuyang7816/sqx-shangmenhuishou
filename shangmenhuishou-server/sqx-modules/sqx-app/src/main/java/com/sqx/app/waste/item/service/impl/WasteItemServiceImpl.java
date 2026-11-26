package com.sqx.app.waste.item.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.app.waste.category.db.service.IWasteCategoryService;
import com.sqx.app.waste.item.db.service.IWasteItemService;
import com.sqx.app.waste.item.domain.bo.*;
import com.sqx.app.waste.item.domain.entity.WasteItem;
import com.sqx.app.waste.item.domain.vo.WasteItemVo;
import com.sqx.app.waste.item.service.WasteItemService;
import com.sqx.common.core.exception.BizException;
import com.sqx.common.mybatis.utils.PageUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 回收项目 服务实现类
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-28 15:47:04
 */
@Service
public class WasteItemServiceImpl implements WasteItemService {

    @Resource
    private IWasteItemService iWasteItemService;
    @Resource
    private IWasteCategoryService iWasteCategoryService;

    @Override
    public void saveInfo(WasteItemSaveBo wasteItemSaveBo) {
        WasteItem wasteItem = new WasteItem();
        BeanUtils.copyProperties(wasteItemSaveBo, wasteItem);

        iWasteItemService.save(wasteItem);
    }

    @Override
    public IPage<WasteItemVo> pageInfo(WasteItemPageBo wasteItemPageBo) {
        Page<WasteItem> initPage = PageUtil.initPage(wasteItemPageBo.getPage(), wasteItemPageBo.getLimit());

        IPage<WasteItemVo> iPage = iWasteItemService.pageInfo(initPage, wasteItemPageBo);
        return iPage;
    }

    @Override
    public WasteItemVo info(WasteItemInfoBo wasteItemInfoBo) {
        WasteItemVo wasteItem = iWasteItemService.info(wasteItemInfoBo.getId());
        if (Objects.isNull(wasteItem)) {
            throw new BizException("信息不存在");
        }

        return wasteItem;
    }

    @Override
    public void updateInfo(WasteItemUpdateBo wasteItemUpdateBo) {
        WasteItem wasteItem = new WasteItem();
        BeanUtils.copyProperties(wasteItemUpdateBo, wasteItem);

        iWasteItemService.updateById(wasteItem);
    }

    @Override
    public void delete(WasteItemInfoBo wasteItemInfoBo) {
        iWasteItemService.removeById(wasteItemInfoBo.getId());
    }

    @Override
    public IPage<WasteItemVo> pageByCategoryId(WasteItemPageByCategoryIdBo wasteItemPageByCategoryIdBo) {
        Page<WasteItem> initPage = PageUtil.initPage(wasteItemPageByCategoryIdBo.getPage(), wasteItemPageByCategoryIdBo.getLimit());

        IPage<WasteItemVo> voIPage = iWasteItemService.pageByCategoryIds(initPage, wasteItemPageByCategoryIdBo.getCategoryId());
        return voIPage;
    }
}
