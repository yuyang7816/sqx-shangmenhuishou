package com.sqx.app.address.db.service.impl;

import com.sqx.app.address.domain.entity.Address;
import com.sqx.app.address.db.mapper.AddressMapper;
import com.sqx.app.address.db.service.IAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 地址 服务实现类
 * </p>
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-17 13:54:22
 */
@Service
public class IAddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements IAddressService {

}
