package com.sqx.app.authentication.db.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.app.authentication.domain.entity.UserAuthentication;

/**
 * <p>
 * 用户实名认证 服务类
 * </p>
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-01 14:08:36
 */
public interface IUserAuthenticationService extends IService<UserAuthentication> {

    boolean check(Long userId);
}
