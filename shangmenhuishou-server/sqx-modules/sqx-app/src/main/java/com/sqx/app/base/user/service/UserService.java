package com.sqx.app.base.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.app.base.user.domain.bo.*;
import com.sqx.app.base.user.domain.vo.UserVo;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.db.entity.UserEntity;

/**
 * @author chengming
 * @since 2025/4/24
 */
public interface UserService {
    Result<UserVo> info(Long userId);

    Result<Void> forgetPassword(ForgetPasswordBO forgetPasswordBO);

    Page<UserEntity> list(ListUserBO listUserBO);

    void update(UpdateUserBO updateUserBO);

    void refundDeposit(RefundDepositBo refundDepositBo);

    Result<Void> delete(Long userId);

    void updateDeposit(UpdateUserDepositBO updateUserDepositBO);

    void updateCid(String cid);
}
