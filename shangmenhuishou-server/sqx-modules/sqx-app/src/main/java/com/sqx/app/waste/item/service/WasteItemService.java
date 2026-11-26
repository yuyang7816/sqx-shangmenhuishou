package com.sqx.app.waste.item.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.waste.item.domain.bo.*;
import com.sqx.app.waste.item.domain.vo.WasteItemVo;

/**
 * 回收项目 服务类
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-28 15:47:04
 */
public interface WasteItemService {

    void saveInfo(WasteItemSaveBo wasteItemSaveBo);

    IPage<WasteItemVo> pageInfo(WasteItemPageBo wasteItemPageBo);

    WasteItemVo info(WasteItemInfoBo wasteItemInfoBo);

    void updateInfo(WasteItemUpdateBo wasteItemUpdateBo);

    void delete(WasteItemInfoBo wasteItemInfoBo);

    IPage<WasteItemVo> pageByCategoryId(WasteItemPageByCategoryIdBo wasteItemPageByCategoryIdBo);

}
