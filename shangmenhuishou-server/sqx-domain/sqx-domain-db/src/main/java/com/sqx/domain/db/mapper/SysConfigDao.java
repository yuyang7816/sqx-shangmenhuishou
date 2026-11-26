package com.sqx.domain.db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.domain.db.entity.SysConfigEntity;
import org.apache.ibatis.annotations.Mapper;
/**
 * 系统配置信息
 *
 */
@Mapper
public interface SysConfigDao extends BaseMapper<SysConfigEntity> {
	
}
