package com.sqx.app.waste.category.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.waste.category.domain.bo.*;
import com.sqx.app.waste.category.domain.vo.WasteCategoryVo;

import java.util.List;

/**
 * 废品类别 服务类
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-03 11:36:52
 */
public interface WasteCategoryService {

    void saveInfo(WasteCategorySaveBo wasteCategorySaveBo);

    IPage<WasteCategoryVo> pageInfo(WasteCategoryPageBo wasteCategoryPageBo);

    WasteCategoryVo info(WasteCategoryInfoBo wasteCategoryInfoBo);

    void updateInfo(WasteCategoryUpdateBo wasteCategoryUpdateBo);

    void delete(WasteCategoryInfoBo wasteCategoryInfoBo);

    List<WasteCategoryVo> listInfo(WasteCategoryListBo wasteCategoryListBo);
}
