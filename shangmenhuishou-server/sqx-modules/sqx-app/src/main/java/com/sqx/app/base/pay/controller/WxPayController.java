package com.sqx.app.base.pay.controller;

import com.sqx.app.base.constants.BaseConstant;
import com.sqx.app.base.pay.domain.BusinessParam;
import com.sqx.app.base.pay.service.WxPayService;
import com.sqx.common.core.domain.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chengming
 * @since 2025/4/22
 */
@Tag(name = "微信支付", description = "微信支付")
@RestController
@RequestMapping(BaseConstant.BASE_APP_PREFIX + "/wxpay")
public class WxPayController {

    @Autowired
    private WxPayService wxPayService;

    @Operation(summary = "拉起微信支付")
    @PostMapping("pay")
    public <T> Result<T> pay(@RequestBody BusinessParam payParam) {
        return wxPayService.pay(payParam);
    }

    @Operation(summary = "微信回调", hidden = true)
    @PostMapping("notifyUrl")
    public String notifyUrl(HttpServletRequest request) {
        return wxPayService.notifyUrl(request);
    }
}
