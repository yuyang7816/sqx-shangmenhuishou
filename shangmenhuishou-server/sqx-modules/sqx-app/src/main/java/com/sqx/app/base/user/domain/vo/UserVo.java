package com.sqx.app.base.user.domain.vo;

import com.sqx.app.referral.withdraw.domain.entity.WithdrawalAccount;
import com.sqx.domain.db.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author chengming
 * @since 2025/10/15
 */
@Data
public class UserVo extends UserEntity {

    @Schema(description = "绑定的提现账号")
    private List<WithdrawalAccount> withdrawalAccounts;

    @Schema(description = "是否认证")
    private Boolean isAuthentication;

    @Schema(description = "是否缴纳保证金")
    private Boolean isDeposit;

    @Schema(description = "钱包余额")
    private BigDecimal balance;
}
