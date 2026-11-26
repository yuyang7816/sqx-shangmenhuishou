package com.sqx.app.base.pay.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.sqx.app.base.pay.business.service.CallbackService;
import com.sqx.app.base.pay.enums.BusinessType;
import com.sqx.app.base.utils.SysConfigUtil;
import com.sqx.app.deposit.db.service.IDepositRecordService;
import com.sqx.app.payment.record.db.service.IUserPaymentRecordService;
import com.sqx.app.payment.record.domain.entity.UserPaymentRecord;
import com.sqx.common.core.exception.BizException;
import com.sqx.domain.db.entity.UserEntity;
import com.sqx.domain.db.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author chengming
 * @since 2025/4/21
 *
 * 回调业务
 */
@Slf4j
@Service
public class CallbackServiceImpl implements CallbackService {

    @Resource
    private IUserPaymentRecordService iUserPaymentRecordService;
    @Resource
    private IUserService iUserService;
    @Resource
    private IDepositRecordService iDepositRecordService;

    @Override
    public void successProcess(String orderNo, String tradeNo) {
        log.info("回调业务处理");

        UserPaymentRecord userPaymentRecord = getUserPaymentRecord(orderNo);
        if (Objects.equals(userPaymentRecord.getBizType(), BusinessType.deposit.getCode())) {
            deposit(userPaymentRecord, tradeNo);
        }
    }

    /**
     * 店铺保证金
     * @param userPaymentRecord
     * @param tradeNo
     */
    private void deposit(UserPaymentRecord userPaymentRecord, String tradeNo) {
        String deposit = SysConfigUtil.getConfigValue("biz_deposit_amount");

        UserEntity userEntity = iUserService.getById(userPaymentRecord.getUserId());
        if (Objects.isNull(userEntity)) {
            throw new BizException("用户信息不存在");
        }

        iUserService.incrDeposit(userPaymentRecord.getUserId(), new BigDecimal(deposit));
        iDepositRecordService.saveDeposit(userPaymentRecord.getUserId(), userPaymentRecord.getAmount());

        // 修改支付信息
        updateUserPaymentRecord(userPaymentRecord.getId(), tradeNo);
    }

    private void updateUserPaymentRecord(Long id, String tradeNo) {
        UpdateWrapper<UserPaymentRecord> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(UserPaymentRecord::getId, id)
                .set(UserPaymentRecord::getTradeNo, tradeNo)
                .set(UserPaymentRecord::getStatus, 1)
                .set(UserPaymentRecord::getPayTime, LocalDateTime.now())
                .set(UserPaymentRecord::getNotifyCount, 1)
                .set(UserPaymentRecord::getLastNotifyTime, LocalDateTime.now());

        iUserPaymentRecordService.update(updateWrapper);
    }

    private UserPaymentRecord getUserPaymentRecord(String orderNo) {
        QueryWrapper<UserPaymentRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserPaymentRecord::getOrderNo, orderNo);

        UserPaymentRecord userPaymentRecord = iUserPaymentRecordService.getOne(queryWrapper);
        if (Objects.isNull(userPaymentRecord)) {
            throw new BizException("支付详情不存在");
        }

        return userPaymentRecord;
    }
}
