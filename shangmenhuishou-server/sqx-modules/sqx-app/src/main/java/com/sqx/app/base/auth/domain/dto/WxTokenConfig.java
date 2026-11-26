package com.sqx.app.base.auth.domain.dto;

import lombok.Data;

/**
 * @author chengming
 * @since 2025/11/18
 */
@Data
public class WxTokenConfig {

    private String appId;

    private String secret;
}
