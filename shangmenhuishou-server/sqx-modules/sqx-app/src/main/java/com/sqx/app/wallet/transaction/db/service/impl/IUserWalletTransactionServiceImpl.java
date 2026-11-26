package com.sqx.app.wallet.transaction.db.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.app.base.enums.DepositOperationType;
import com.sqx.app.wallet.transaction.db.mapper.UserWalletTransactionMapper;
import com.sqx.app.wallet.transaction.db.service.IUserWalletTransactionService;
import com.sqx.app.wallet.transaction.domain.entity.UserWalletTransaction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * <p>
 * 用户钱包交易记录表 服务实现类
 * </p>
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-08-13 15:12:03
 */
@Service
public class IUserWalletTransactionServiceImpl extends ServiceImpl<UserWalletTransactionMapper, UserWalletTransaction> implements IUserWalletTransactionService {

    @Override
    public void adminUpdate(Long userId, BigDecimal amount, String operationType) {
        UserWalletTransaction userWalletTransaction = new UserWalletTransaction();
        userWalletTransaction.setUserId(userId);
        userWalletTransaction.setAmount(amount);
        userWalletTransaction.setTransactionType(5);
        userWalletTransaction.setStatus(2);
        userWalletTransaction.setTitle("系统调整");
        userWalletTransaction.setDescription("系统调整余额，金额: " + amount);

        if (Objects.equals(DepositOperationType.incr.getCode(), operationType)) {
            userWalletTransaction.setDirection(1);
        } else if (Objects.equals(DepositOperationType.decr.getCode(), operationType)) {
            userWalletTransaction.setDirection(2);
        }

        this.save(userWalletTransaction);
    }
}
