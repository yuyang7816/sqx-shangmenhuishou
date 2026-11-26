package com.sqx.app.waste.category.db.service;

import com.sqx.app.waste.category.domain.entity.WasteCategory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 废品类别 服务类
 * </p>
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-03 11:36:52
 */
public interface IWasteCategoryService extends IService<WasteCategory> {

    List<WasteCategory> getChildren(Long categoryId);
}
