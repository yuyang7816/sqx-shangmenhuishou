package com.sqx.app.address.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.app.address.db.service.IAddressService;
import com.sqx.app.address.domain.bo.*;
import com.sqx.app.address.domain.entity.Address;
import com.sqx.app.address.domain.vo.AddressVo;
import com.sqx.app.address.service.AddressService;
import com.sqx.common.core.exception.BizException;
import com.sqx.common.mybatis.utils.PageUtil;
import com.sqx.common.shiro.utils.UserContextHolder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 地址 服务实现类
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-17 13:54:22
 */
@Service
public class AddressServiceImpl implements AddressService {

    @Resource
    private IAddressService iAddressService;

    @Override
    public void saveInfo(AddressSaveBo addressSaveBo) {
        Address address = new Address();
        BeanUtils.copyProperties(addressSaveBo, address);
        address.setUserId(UserContextHolder.getAppUserId());

        if (1 == addressSaveBo.getIsDefault() && checkDefaultExists(UserContextHolder.getAppUserId())) {
            // 修改之前的默认地址为非默认
            LambdaUpdateWrapper<Address> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper
                    .eq(Address::getUserId, UserContextHolder.getAppUserId())
                    .eq(Address::getIsDefault, 1)
                    .set(Address::getIsDefault, 2);

            iAddressService.update(updateWrapper);
        }

        iAddressService.save(address);
    }

    private boolean checkDefaultExists(Long userId) {
        LambdaQueryWrapper<Address> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Address::getUserId, userId)
                .eq(Address::getIsDefault, 1);

        return iAddressService.exists(queryWrapper);
    }

    @Override
    public IPage<AddressVo> pageInfo(AddressPageBo addressPageBo) {
        Page<Address> initPage = PageUtil.initPage(addressPageBo.getPage(), addressPageBo.getLimit());

        QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Objects.nonNull(addressPageBo.getUserId()), Address::getUserId, addressPageBo.getUserId())
                .like(StringUtils.isNotBlank(addressPageBo.getName()), Address::getName, addressPageBo.getName())
                .like(StringUtils.isNotBlank(addressPageBo.getPhone()), Address::getPhone, addressPageBo.getPhone())
                .eq(Objects.nonNull(addressPageBo.getIsDefault()), Address::getIsDefault, addressPageBo.getIsDefault())
                .orderByDesc(Address::getCreateTime);

        IPage<Address> iPage = iAddressService.page(initPage, queryWrapper);
        List<AddressVo> voList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
            for (Address record : iPage.getRecords()) {
                AddressVo vo = new AddressVo();
                BeanUtils.copyProperties(record, vo);

                voList.add(vo);
            }
        }

        IPage<AddressVo> voIPage = PageUtil.copyPage(iPage, voList);
        return voIPage;
    }

    @Override
    public AddressVo info(AddressInfoBo addressInfoBo) {
        Address address = iAddressService.getById(addressInfoBo.getId());
        if (Objects.isNull(address)) {
            throw new BizException("信息不存在");
        }

        AddressVo vo = new AddressVo();
        BeanUtils.copyProperties(address, vo);
        return vo;
    }

    @Override
    public void updateInfo(AddressUpdateBo addressUpdateBo) {
        Address address = new Address();
        BeanUtils.copyProperties(addressUpdateBo, address);

        iAddressService.updateById(address);
    }

    @Override
    public void delete(AddressInfoBo addressInfoBo) {
        iAddressService.removeById(addressInfoBo.getId());
    }

    @Override
    public void updateDefault(AddressUpdateDefaultBo addressUpdateDefaultBo) {
        LambdaQueryWrapper<Address> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .ne(Address::getId, addressUpdateDefaultBo.getId())
                .eq(Address::getUserId, addressUpdateDefaultBo.getUserId())
                .eq(Address::getIsDefault, 1);

        Address address = iAddressService.getOne(queryWrapper);
        if (Objects.nonNull(address)) {
            address.setIsDefault(2);
            iAddressService.updateById(address);
        }

        LambdaUpdateWrapper<Address> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .eq(Address::getId, addressUpdateDefaultBo.getId())
                .set(Address::getIsDefault, addressUpdateDefaultBo.getIsDefault());

        iAddressService.update(new Address(), updateWrapper);
    }

    @Override
    public AddressVo getDefault() {
        LambdaQueryWrapper<Address> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Address::getUserId, UserContextHolder.getAppUserId())
                .eq(Address::getIsDefault, 1);

        Address address = iAddressService.getOne(queryWrapper);
        if (Objects.isNull(address)) {
            return null;
        }

        AddressVo vo = new AddressVo();
        BeanUtils.copyProperties(address, vo);
        return vo;
    }
}
