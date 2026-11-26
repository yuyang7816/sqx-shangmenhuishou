package com.sqx.app.waste.item.db.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.app.waste.item.domain.bo.WasteItemPageBo;
import com.sqx.app.waste.item.domain.entity.WasteItem;
import com.sqx.app.waste.item.domain.vo.WasteItemVo;

/**
 * <p>
 * 回收项目 服务类
 * </p>
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-28 15:47:04
 */
public interface IWasteItemService extends IService<WasteItem> {

    IPage<WasteItemVo> pageInfo(Page<WasteItem> initPage, WasteItemPageBo wasteItemPageBo);

    WasteItemVo info(Long id);

    IPage<WasteItemVo> pageByCategoryIds(Page<WasteItem> initPage, Long categoryId);
}
