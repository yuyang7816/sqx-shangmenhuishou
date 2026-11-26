package com.sqx.domain.db.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.domain.db.entity.SysConfigEntity;

/**
 * 系统配置信息
 *
 */
public interface ISysConfigService extends IService<SysConfigEntity> {


    String getConfigValue(String configKey);

    void loadSysConfig();
}
