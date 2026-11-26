package com.sqx.domain.db.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.domain.db.entity.SysUserTokenEntity;
import com.sqx.domain.db.mapper.SysUserTokenDao;
import com.sqx.domain.db.service.ISysUserTokenService;
import org.springframework.stereotype.Service;


@Service
public class ISysUserTokenServiceImpl extends ServiceImpl<SysUserTokenDao, SysUserTokenEntity> implements ISysUserTokenService {

}
