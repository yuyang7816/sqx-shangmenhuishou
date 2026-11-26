package com.sqx.app.waste.item.db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.app.waste.item.domain.bo.WasteItemPageBo;
import com.sqx.app.waste.item.domain.entity.WasteItem;
import com.sqx.app.waste.item.domain.vo.WasteItemVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 回收项目 Mapper 接口
 * </p>
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-28 15:47:04
 */
public interface WasteItemMapper extends BaseMapper<WasteItem> {

    IPage<WasteItemVo> pageInfo(Page<WasteItem> initPage, @Param("param") WasteItemPageBo wasteItemPageBo);

    WasteItemVo info(@Param("id") Long id);

    IPage<WasteItemVo> pageByCategoryIds(Page<WasteItem> initPage, @Param("categoryId") Long categoryId);
}
