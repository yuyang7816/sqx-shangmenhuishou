package com.sqx.domain.db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.domain.db.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * 用户
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {

    void decrDeposit(@Param("userId") Long userId, @Param("amount") BigDecimal amount);

    void incrDeposit(@Param("userId") Long userId, @Param("amount") BigDecimal amount);
}
