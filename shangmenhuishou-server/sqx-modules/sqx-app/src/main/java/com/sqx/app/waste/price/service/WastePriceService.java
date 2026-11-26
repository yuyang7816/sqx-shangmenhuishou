package com.sqx.app.waste.price.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.waste.price.domain.bo.*;
import com.sqx.app.waste.price.domain.vo.WastePriceVo;

import java.util.List;

/**
 * 废品价格 服务类
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-03 11:37:16
 */
public interface WastePriceService {

    void saveInfo(WastePriceSaveBo wastePriceSaveBo);

    IPage<WastePriceVo> pageInfo(WastePricePageBo wastePricePageBo);

    WastePriceVo info(WastePriceInfoBo wastePriceInfoBo);

    void updateInfo(WastePriceUpdateBo wastePriceUpdateBo);

    void delete(WastePriceInfoBo wastePriceInfoBo);

    List<WastePriceVo> listInfo(WastePriceListBo wastePriceListBo);
}
