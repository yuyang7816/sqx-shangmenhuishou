package com.sqx.domain.db.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.domain.db.entity.SysRoleEntity;
import com.sqx.domain.db.mapper.SysRoleDao;
import com.sqx.domain.db.service.ISysRoleService;
import org.springframework.stereotype.Service;

/**
 * 角色
 *
 */
@Service
public class ISysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRoleEntity> implements ISysRoleService {

}
