package com.sqx.domain.db.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.domain.db.entity.SysCaptchaEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 验证码
 *
 */
@Mapper
public interface SysCaptchaDao extends BaseMapper<SysCaptchaEntity> {

}
