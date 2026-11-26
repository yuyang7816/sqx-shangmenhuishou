package com.sqx.common.redis.utils;

import com.alibaba.fastjson2.JSONObject;
import com.sqx.common.core.exception.BizException;

/**
 * @author chengming
 * @since 2025/5/14
 */
public class ConfigUtil {
    public static String getConfigValue(String paramKey) {
        String key = RedisKeys.SYS_CONFIG_KEY + paramKey;
        if (!RedisUtil.hasKey(key)) {
            throw new BizException("配置不存在");
        }

        String config = RedisUtil.getCacheObject(key);
        JSONObject configJson = JSONObject.parseObject(config);
        return configJson.getString("paramValue");
    }
}
