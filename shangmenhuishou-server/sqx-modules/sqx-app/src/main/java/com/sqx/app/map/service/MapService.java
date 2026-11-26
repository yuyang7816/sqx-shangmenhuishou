package com.sqx.app.map.service;


import com.sqx.app.map.domain.vo.AddressVo;

/**
 * @author chengming
 * @since 2025/7/22
 */
public interface MapService {
    AddressVo reverseGeocode(String lat, String lng);
}
