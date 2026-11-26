package com.sqx.app.base.pay.controller;

import com.sqx.app.base.constants.BaseConstant;
import com.sqx.app.base.pay.domain.BusinessParam;
import com.sqx.common.core.domain.Result;
import com.sqx.app.base.pay.service.AlipayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chengming
 * @since 2025/4/21
 */
@Tag(name = "支付宝接口", description = "支付宝支付接口")
@RestController
@RequestMapping(BaseConstant.BASE_APP_PREFIX + "/alipay")
public class AlipayController {

    @Autowired
    private AlipayService alipayService;

    @Operation(summary = "支付宝手机网站支付")
    @PostMapping("wapPay")
    public Result<String> wapPay(@RequestBody @Validated BusinessParam param) {
        return alipayService.wapPay(param);
    }

    @Operation(summary = "支付宝app支付")
    @PostMapping("appPay")
    public Result<String> appPay(@RequestBody @Validated BusinessParam param) {
        return alipayService.appPay(param);
    }

    @Operation(summary = "支付宝pc支付(电脑网页扫码支付)")
    @PostMapping("pagePay")
    public Result<String> pagePay(@RequestBody @Validated BusinessParam param) {
        return alipayService.pagePay(param);
    }

    @Operation(summary = "支付宝普通公钥模式同步通知接口", hidden = true)
    @PostMapping("notifyUrl")
    public String notifyUrl(HttpServletRequest request) {
        return alipayService.notifyUrl(request);
    }

    @Operation(summary = "支付宝证书模式同步通知接口", hidden = true)
    @PostMapping("certNotifyUrl")
    public String certNotifyUrl(HttpServletRequest request) {
        return alipayService.certNotifyUrl(request);
    }

    @Operation(summary = "支付宝普通公钥模式异步通知接口", hidden = true)
    @GetMapping("returnUrl")
    public String returnUrl(HttpServletRequest request) {
        return alipayService.returnUrl(request);
    }

    @Operation(summary = "支付宝证书模式异步通知接口", hidden = true)
    @GetMapping("certReturnUrl")
    public String certReturnUrl(HttpServletRequest request) {
        return alipayService.certReturnUrl(request);
    }
}
