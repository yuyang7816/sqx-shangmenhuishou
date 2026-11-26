package com.sqx.domain.db.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.domain.db.entity.SysUserEntity;
import com.sqx.domain.db.mapper.SysUserDao;
import com.sqx.domain.db.service.ISysUserService;
import org.springframework.stereotype.Service;


/**
 * 系统用户
 *
 */
@Service
public class ISysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements ISysUserService {

}