package com.sqx.app.map.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sqx.app.base.utils.http.HttpClientUtil;
import com.sqx.app.map.domain.vo.AddressVo;
import com.sqx.app.map.factory.MapFactory;
import com.sqx.app.map.service.MapService;
import com.sqx.app.map.strategy.IMapPlatformStrategy;
import com.sqx.domain.db.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chengming
 * @since 2025/7/22
 */
@Slf4j
@Service
public class MapServiceImpl implements MapService {

    @Resource
    private ISysConfigService iSysConfigService;

    @Override
    public AddressVo reverseGeocode(String lat, String lng) {
        String mapPlatform = iSysConfigService.getConfigValue("map_platform");

        MapFactory mapFactory = MapFactory.getInstance();
        IMapPlatformStrategy platform = mapFactory.getPlatform(mapPlatform);

        return platform.reverseGeocode(lat, lng);
    }
}
