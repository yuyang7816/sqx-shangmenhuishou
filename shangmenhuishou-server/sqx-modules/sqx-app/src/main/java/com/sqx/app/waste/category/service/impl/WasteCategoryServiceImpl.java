package com.sqx.app.waste.category.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.app.waste.category.db.service.IWasteCategoryService;
import com.sqx.app.waste.category.domain.bo.*;
import com.sqx.app.waste.category.domain.entity.WasteCategory;
import com.sqx.app.waste.category.domain.vo.WasteCategoryVo;
import com.sqx.app.waste.category.service.WasteCategoryService;
import com.sqx.common.core.exception.BizException;
import com.sqx.common.mybatis.utils.PageUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 废品类别 服务实现类
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-03 11:36:52
 */
@Service
public class WasteCategoryServiceImpl implements WasteCategoryService {

    @Resource
    private IWasteCategoryService iWasteCategoryService;

    @Override
    public void saveInfo(WasteCategorySaveBo wasteCategorySaveBo) {
        if (checkExistsByName(wasteCategorySaveBo.getName(), wasteCategorySaveBo.getLevel())) {
            throw new BizException("类别已存在");
        }

        WasteCategory wasteCategory = new WasteCategory();
        BeanUtils.copyProperties(wasteCategorySaveBo, wasteCategory);

        wasteCategory.setAppointmentNum(0);
        iWasteCategoryService.save(wasteCategory);
    }

    private boolean checkExistsByName(String name, Integer level) {
        LambdaQueryWrapper<WasteCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(WasteCategory::getName, name)
                .eq(WasteCategory::getLevel, level);

        return iWasteCategoryService.exists(queryWrapper);
    }

    @Override
    public IPage<WasteCategoryVo> pageInfo(WasteCategoryPageBo wasteCategoryPageBo) {
        Page<WasteCategory> initPage = PageUtil.initPage(wasteCategoryPageBo.getPage(), wasteCategoryPageBo.getLimit());

        QueryWrapper<WasteCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Objects.nonNull(wasteCategoryPageBo.getParentId()), WasteCategory::getParentId, wasteCategoryPageBo.getParentId())
                .eq(StringUtils.isNotBlank(wasteCategoryPageBo.getName()), WasteCategory::getName, wasteCategoryPageBo.getName())
                .eq(Objects.nonNull(wasteCategoryPageBo.getLevel()), WasteCategory::getLevel, wasteCategoryPageBo.getLevel())
                .orderByDesc(WasteCategory::getCreateTime);

        IPage<WasteCategory> iPage = iWasteCategoryService.page(initPage, queryWrapper);
        List<WasteCategoryVo> voList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
            for (WasteCategory record : iPage.getRecords()) {
                WasteCategoryVo vo = new WasteCategoryVo();
                BeanUtils.copyProperties(record, vo);

                voList.add(vo);
            }
        }

        IPage<WasteCategoryVo> voIPage = PageUtil.copyPage(iPage, voList);
        return voIPage;
    }

    @Override
    public WasteCategoryVo info(WasteCategoryInfoBo wasteCategoryInfoBo) {
        WasteCategory wasteCategory = iWasteCategoryService.getById(wasteCategoryInfoBo.getId());
        if (Objects.isNull(wasteCategory)) {
            throw new BizException("信息不存在");
        }

        WasteCategoryVo vo = new WasteCategoryVo();
        BeanUtils.copyProperties(wasteCategory, vo);
        return vo;
    }

    @Override
    public void updateInfo(WasteCategoryUpdateBo wasteCategoryUpdateBo) {
        if (checkExistsByName(wasteCategoryUpdateBo.getName(), wasteCategoryUpdateBo.getLevel())) {
            throw new BizException("类别已存在");
        }

        WasteCategory wasteCategory = new WasteCategory();
        BeanUtils.copyProperties(wasteCategoryUpdateBo, wasteCategory);

        iWasteCategoryService.updateById(wasteCategory);
    }

    @Override
    public void delete(WasteCategoryInfoBo wasteCategoryInfoBo) {
        iWasteCategoryService.removeById(wasteCategoryInfoBo.getId());
    }

    @Override
    public List<WasteCategoryVo> listInfo(WasteCategoryListBo wasteCategoryListBo) {
        QueryWrapper<WasteCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Objects.nonNull(wasteCategoryListBo.getParentId()), WasteCategory::getParentId, wasteCategoryListBo.getParentId())
                .eq(StringUtils.isNotBlank(wasteCategoryListBo.getName()), WasteCategory::getName, wasteCategoryListBo.getName())
                .eq(Objects.nonNull(wasteCategoryListBo.getLevel()), WasteCategory::getLevel, wasteCategoryListBo.getLevel())
                .eq(WasteCategory::getParentId, 0)
                .orderByDesc(WasteCategory::getCreateTime);

        List<WasteCategory> categoryList = iWasteCategoryService.list(queryWrapper);
        List<WasteCategoryVo> voList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(categoryList)) {
            for (WasteCategory record : categoryList) {
                WasteCategoryVo vo = new WasteCategoryVo();
                BeanUtils.copyProperties(record, vo);

                List<WasteCategory> children = getChildren(vo.getId());
                vo.setChildren(children);

                voList.add(vo);
            }
        }

        return voList;
    }

    public List<WasteCategory> getChildren(Long parentId) {
        List<WasteCategory> result = new ArrayList<>();
        List<WasteCategory> directChildren = iWasteCategoryService.list(new LambdaQueryWrapper<WasteCategory>().eq(WasteCategory::getParentId, parentId));
        for (WasteCategory child : directChildren) {
            result.add(child);
            result.addAll(getChildren(child.getId())); // 递归
        }

        return result;
    }
}
