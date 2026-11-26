package com.sqx.app.base.pay.service;

import com.sqx.app.base.pay.domain.BusinessParam;
import com.sqx.common.core.domain.Result;
import com.sqx.app.base.pay.domain.AliPayParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chengming
 * @since 2025/4/21
 */
public interface AlipayService {

    Result<String> wapPay(BusinessParam param);

    Result<String> appPay(BusinessParam param);

    Result<String> pagePay(BusinessParam param);

    String notifyUrl(HttpServletRequest request);

    String certNotifyUrl(HttpServletRequest request);

    String returnUrl(HttpServletRequest request);

    String certReturnUrl(HttpServletRequest request);
}
