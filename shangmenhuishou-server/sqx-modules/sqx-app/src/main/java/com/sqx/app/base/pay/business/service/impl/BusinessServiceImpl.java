package com.sqx.app.base.pay.business.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.sqx.app.base.pay.business.service.BusinessService;
import com.sqx.app.base.pay.domain.BusinessParam;
import com.sqx.app.base.pay.enums.BusinessType;
import com.sqx.app.payment.record.db.service.IUserPaymentRecordService;
import com.sqx.app.payment.record.domain.entity.UserPaymentRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;

/**
 * @author chengming
 * @since 2025/4/21
 */
@Slf4j
@Service
public class BusinessServiceImpl implements BusinessService {

    @Resource
    private IUserPaymentRecordService iUserPaymentRecordService;

    @Override
    public void process(BusinessParam param) {
        log.info("支付业务处理");

        if (Objects.equals(param.getBusinessType(), BusinessType.deposit.getCode())) {
            // 缴纳保证金
            deposit(param);
        }
    }

    /**
     * 缴纳保证金
     * @param param
     */
    private void deposit(BusinessParam param) {
        Map<String, Object> extensions = param.getExtensions();

        UserPaymentRecord userPaymentRecord = new UserPaymentRecord();
        userPaymentRecord.setOrderNo(param.getOrderNo());
        userPaymentRecord.setUserId(param.getUserId());
        userPaymentRecord.setAmount(param.getAmount());
        userPaymentRecord.setPaymentChannel(param.getPlatform());
        userPaymentRecord.setPaymentMethod(param.getTradeType());
        userPaymentRecord.setBizType(param.getBusinessType());
        userPaymentRecord.setStatus(0);
        userPaymentRecord.setExtra(JSONObject.toJSONString(extensions));

        iUserPaymentRecordService.save(userPaymentRecord);
    }
}
