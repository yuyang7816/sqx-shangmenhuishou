package com.sqx.app.waste.category.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.app.waste.category.db.mapper.WasteCategoryMapper;
import com.sqx.app.waste.category.db.service.IWasteCategoryService;
import com.sqx.app.waste.category.domain.entity.WasteCategory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 废品类别 服务实现类
 * </p>
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-03 11:36:52
 */
@Service
public class IWasteCategoryServiceImpl extends ServiceImpl<WasteCategoryMapper, WasteCategory> implements IWasteCategoryService {

    @Override
    public List<WasteCategory> getChildren(Long categoryId) {
        LambdaQueryWrapper<WasteCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WasteCategory::getParentId, categoryId);

        return this.list(queryWrapper);
    }
}
