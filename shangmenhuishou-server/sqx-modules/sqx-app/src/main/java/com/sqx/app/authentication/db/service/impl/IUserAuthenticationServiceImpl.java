package com.sqx.app.authentication.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.app.authentication.db.mapper.UserAuthenticationMapper;
import com.sqx.app.authentication.db.service.IUserAuthenticationService;
import com.sqx.app.authentication.domain.entity.UserAuthentication;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户实名认证 服务实现类
 * </p>
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-01 14:08:36
 */
@Service
public class IUserAuthenticationServiceImpl extends ServiceImpl<UserAuthenticationMapper, UserAuthentication> implements IUserAuthenticationService {

    @Override
    public boolean check(Long userId) {
        LambdaQueryWrapper<UserAuthentication> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(UserAuthentication::getUserId, userId)
                .eq(UserAuthentication::getStatus, 2);

        return this.exists(queryWrapper);
    }
}
