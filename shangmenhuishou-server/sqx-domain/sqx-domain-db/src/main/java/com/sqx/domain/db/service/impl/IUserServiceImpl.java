package com.sqx.domain.db.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.domain.db.entity.UserEntity;
import com.sqx.domain.db.mapper.UserDao;
import com.sqx.domain.db.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;


/**
 * 用户
 *
 * @author fang
 * @date 2021/2/27
 */

@Service
public class IUserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements IUserService {

    @Resource
    private UserDao userDao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void decrDeposit(Long userId, BigDecimal amount) {
        userDao.decrDeposit(userId, amount);
    }

    @Override
    public void incrDeposit(Long userId, BigDecimal amount) {
        userDao.incrDeposit(userId, amount);
    }

    @Override
    public UserEntity getByOpenId(String openid) {
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserEntity::getMaOpenId, openid);

        return this.getOne(queryWrapper);
    }

    @Override
    public void updateUserType(Long userId, int userType) {
        LambdaUpdateWrapper<UserEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .eq(UserEntity::getUserId, userId)
                .set(UserEntity::getUserType, userType);

        this.update(new UserEntity(), updateWrapper);
    }
}
