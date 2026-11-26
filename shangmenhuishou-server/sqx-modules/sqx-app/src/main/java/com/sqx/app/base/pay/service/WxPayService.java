package com.sqx.app.base.pay.service;

import com.sqx.app.base.pay.domain.AliPayParam;
import com.sqx.app.base.pay.domain.BusinessParam;
import com.sqx.app.base.pay.domain.WxPayParam;
import com.sqx.common.core.domain.Result;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chengming
 * @since 2025/4/22
 */
public interface WxPayService {
    <T> Result<T> pay(BusinessParam payParam);

    String notifyUrl(HttpServletRequest request);
}
