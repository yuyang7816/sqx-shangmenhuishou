package com.sqx.app.waste.item.db.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.app.waste.item.db.mapper.WasteItemMapper;
import com.sqx.app.waste.item.db.service.IWasteItemService;
import com.sqx.app.waste.item.domain.bo.WasteItemPageBo;
import com.sqx.app.waste.item.domain.entity.WasteItem;
import com.sqx.app.waste.item.domain.vo.WasteItemVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 回收项目 服务实现类
 * </p>
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-28 15:47:04
 */
@Service
public class IWasteItemServiceImpl extends ServiceImpl<WasteItemMapper, WasteItem> implements IWasteItemService {

    @Resource
    private WasteItemMapper wasteItemMapper;

    @Override
    public IPage<WasteItemVo> pageInfo(Page<WasteItem> initPage, WasteItemPageBo wasteItemPageBo) {
        return wasteItemMapper.pageInfo(initPage, wasteItemPageBo);
    }

    @Override
    public WasteItemVo info(Long id) {
        return wasteItemMapper.info(id);
    }

    @Override
    public IPage<WasteItemVo> pageByCategoryIds(Page<WasteItem> initPage, Long categoryId) {
        return wasteItemMapper.pageByCategoryIds(initPage, categoryId);
    }
}
