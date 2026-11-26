package com.sqx.common.shiro.service;


import com.sqx.domain.db.entity.SysUserEntity;
import com.sqx.domain.db.entity.SysUserTokenEntity;

import java.util.Set;

/**
 * @author chengming
 * @since 2024/7/11
 */
public interface ShiroService {
    /**
     * 获取用户权限列表
     */
    Set<String> getUserPermissions(long userId);

    SysUserTokenEntity queryByToken(String token);

    /**
     * 根据用户ID，查询用户
     * @param userId
     */
    SysUserEntity queryUser(Long userId);
}
