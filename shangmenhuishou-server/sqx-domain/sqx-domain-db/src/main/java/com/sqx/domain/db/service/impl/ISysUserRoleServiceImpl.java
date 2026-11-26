package com.sqx.domain.db.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.domain.db.entity.SysUserRoleEntity;
import com.sqx.domain.db.mapper.SysUserRoleDao;
import com.sqx.domain.db.service.ISysUserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户与角色对应关系
 *
 */
@Service
public class ISysUserRoleServiceImpl extends ServiceImpl<SysUserRoleDao, SysUserRoleEntity> implements ISysUserRoleService {

}
