package com.sqx.domain.db.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.domain.db.entity.UserEntity;

import java.math.BigDecimal;


/**
 * 用户
 *
 * @author fang
 * @date 2021/2/27
 */
public interface IUserService extends IService<UserEntity> {

    void decrDeposit(Long userId, BigDecimal amount);

    void incrDeposit(Long userId, BigDecimal amount);

    UserEntity getByOpenId(String openid);

    void updateUserType(Long userId, int userType);
}
