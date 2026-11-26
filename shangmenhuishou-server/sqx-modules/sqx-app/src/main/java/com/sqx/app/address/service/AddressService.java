package com.sqx.app.address.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.address.domain.bo.*;
import com.sqx.app.address.domain.vo.AddressVo;

/**
 * 地址 服务类
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-17 13:54:22
 */
public interface AddressService {

    void saveInfo(AddressSaveBo addressSaveBo);

    IPage<AddressVo> pageInfo(AddressPageBo addressPageBo);

    AddressVo info(AddressInfoBo addressInfoBo);

    void updateInfo(AddressUpdateBo addressUpdateBo);

    void delete(AddressInfoBo addressInfoBo);

    void updateDefault(AddressUpdateDefaultBo addressUpdateDefaultBo);

    AddressVo getDefault();
}
