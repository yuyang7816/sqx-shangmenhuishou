package com.sqx.app.referral.withdraw.db.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.app.referral.withdraw.domain.entity.WithdrawRecord;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 提现记录 服务类
 * </p>
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-08-05 10:10:32
 */
public interface IWithdrawRecordService extends IService<WithdrawRecord> {

    BigDecimal sumAmount(LocalDateTime startTime, LocalDateTime endTime);
}
