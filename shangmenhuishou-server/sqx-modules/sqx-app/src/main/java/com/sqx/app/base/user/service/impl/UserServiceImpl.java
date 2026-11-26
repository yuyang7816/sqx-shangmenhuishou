package com.sqx.app.base.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.app.authentication.db.service.IUserAuthenticationService;
import com.sqx.app.base.enums.AppError;
import com.sqx.app.base.enums.DepositOperationType;
import com.sqx.app.base.user.domain.bo.*;
import com.sqx.app.base.user.domain.vo.UserVo;
import com.sqx.app.base.user.service.UserService;
import com.sqx.app.base.verifyCode.service.VerifyCodeService;
import com.sqx.app.deposit.db.service.IDepositRecordService;
import com.sqx.app.referral.withdraw.db.service.IWithdrawalAccountService;
import com.sqx.app.referral.withdraw.domain.entity.WithdrawalAccount;
import com.sqx.app.wallet.transaction.db.service.IUserWalletTransactionService;
import com.sqx.app.wallet.transaction.domain.entity.UserWalletTransaction;
import com.sqx.app.wallet.wallet.db.service.IUserWalletService;
import com.sqx.app.wallet.wallet.domain.entity.UserWallet;
import com.sqx.app.waste.order.order.db.service.IWasteOrderService;
import com.sqx.app.waste.order.order.domain.entity.WasteOrder;
import com.sqx.app.waste.order.order.enums.OrderStatus;
import com.sqx.common.core.domain.Result;
import com.sqx.common.core.exception.BizException;
import com.sqx.common.mybatis.utils.PageUtil;
import com.sqx.common.shiro.utils.UserContextHolder;
import com.sqx.domain.db.entity.UserEntity;
import com.sqx.domain.db.service.IUserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author chengming
 * @since 2025/4/24
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private VerifyCodeService verifyCodeService;
    @Autowired
    private IUserWalletTransactionService iUserWalletTransactionService;
    @Autowired
    private IUserWalletService iUserWalletService;
    @Autowired
    private IUserAuthenticationService iUserAuthenticationService;
    @Autowired
    private IDepositRecordService iDepositRecordService;
    @Autowired
    private IWasteOrderService iWasteOrderService;
    @Autowired
    private IWithdrawalAccountService iWithdrawalAccountService;

    @Override
    public Result<UserVo> info(Long userId) {
        UserEntity userEntity = iUserService.getById(userId);
        if (Objects.isNull(userEntity)) {
            throw new BizException(AppError.USER_NOT_FOUND);
        }

        UserVo vo = new UserVo();
        BeanUtils.copyProperties(userEntity, vo);

        // 是否认证
        boolean authentication = iUserAuthenticationService.check(userId);
        vo.setIsAuthentication(authentication);

        if (Objects.isNull(userEntity.getDeposit()) || userEntity.getDeposit().compareTo(BigDecimal.ZERO) == 0) {
            vo.setIsDeposit(Boolean.FALSE);
        } else {
            vo.setIsDeposit(Boolean.TRUE);
        }

        // 绑定的提现账号
        List<WithdrawalAccount> withdrawalAccounts = iWithdrawalAccountService.getByUserId(userId);
        vo.setWithdrawalAccounts(withdrawalAccounts);

        // 用户钱包余额
        UserWallet userWallet = iUserWalletService.getByUserId(userId);
        if (Objects.nonNull(userWallet)) {
            vo.setBalance(userWallet.getAmount());
        }

        return Result.success(vo);
    }

    @Override
    public Result<Void> forgetPassword(ForgetPasswordBO forgetPasswordBO) {
        UserEntity userEntity = iUserService.getById(forgetPasswordBO.getUserId());
        if (Objects.isNull(userEntity)) {
            throw new BizException(AppError.USER_NOT_FOUND);
        }

        if (!verifyCodeService.verify(userEntity.getPhone(), forgetPasswordBO.getCode())) {
            throw new BizException(AppError.VERIFY_CODE_ERROR);
        }

        iUserService.update(new UserEntity(), new LambdaUpdateWrapper<UserEntity>()
                .eq(UserEntity::getUserId, forgetPasswordBO.getUserId())
                .set(UserEntity::getPassword, DigestUtils.md5Hex(forgetPasswordBO.getPassword()))
        );

        return Result.success();
    }

    @Override
    public Page<UserEntity> list(ListUserBO listUserBO) {
        Page<UserEntity> initPage = PageUtil.initPage(listUserBO.getPage(), listUserBO.getLimit());

        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(listUserBO.getPhone()), UserEntity::getPhone, listUserBO.getPhone())
                .eq(StringUtils.isNotBlank(listUserBO.getUsername()), UserEntity::getUserName, listUserBO.getUsername())
                .eq(Objects.nonNull(listUserBO.getStatus()), UserEntity::getStatus, listUserBO.getStatus())
                .eq(Objects.nonNull(listUserBO.getUserType()), UserEntity::getUserType, listUserBO.getUserType())
                .orderByDesc(UserEntity::getCreateTime);

        Page<UserEntity> userEntityPage = iUserService.page(initPage, queryWrapper);

        return userEntityPage;
    }

    @Override
    public void update(UpdateUserBO updateUserBO) {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(updateUserBO, userEntity);

        iUserService.updateById(userEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void refundDeposit(RefundDepositBo refundDepositBo) {
        UserEntity userEntity = iUserService.getById(refundDepositBo.getUserId());
        if (Objects.isNull(userEntity)) {
            throw new BizException(AppError.USER_NOT_FOUND);
        }

        if (checkUnfinishedOrder(refundDepositBo.getUserId())) {
            throw new BizException("当前有未完成订单，请先完成订单");
        }

        iUserService.decrDeposit(refundDepositBo.getUserId(), refundDepositBo.getAmount());
        iUserWalletService.incr(refundDepositBo.getUserId(), refundDepositBo.getAmount());

        UserWalletTransaction userWalletTransaction = new UserWalletTransaction();
        userWalletTransaction.setUserId(refundDepositBo.getUserId());
        userWalletTransaction.setAmount(refundDepositBo.getAmount());
        userWalletTransaction.setStatus(1);
        userWalletTransaction.setBizId(refundDepositBo.getUserId());
        userWalletTransaction.setBizType(6);
        userWalletTransaction.setTitle("退保证金");
        userWalletTransaction.setDescription("退保证金: " + refundDepositBo.getAmount());
        userWalletTransaction.setTransactionType(6);
        userWalletTransaction.setDirection(1);
        iUserWalletTransactionService.save(userWalletTransaction);

        // 保存保证金明细
        iDepositRecordService.refund(refundDepositBo.getUserId(), refundDepositBo.getAmount());
    }

    private boolean checkUnfinishedOrder(Long userId) {
        List<Integer> statusList = new ArrayList<>();
        statusList.add(OrderStatus.PENDING_SERVICE.getCode());
        statusList.add(OrderStatus.IN_PROGRESS.getCode());

        LambdaQueryWrapper<WasteOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(WasteOrder::getWasteUserId, userId)
                .in(WasteOrder::getStatus, statusList);

        return iWasteOrderService.exists(queryWrapper);
    }

    @Override
    public Result<Void> delete(Long userId) {
        iUserService.removeById(userId);
        iUserWalletService.remove(new LambdaQueryWrapper<UserWallet>().eq(UserWallet::getUserId, userId));

        return Result.success();
    }

    @Override
    public void updateDeposit(UpdateUserDepositBO updateUserDepositBO) {
        if (Objects.equals(DepositOperationType.incr.getCode(), updateUserDepositBO.getOperationType())) {
            iUserService.incrDeposit(updateUserDepositBO.getUserId(), updateUserDepositBO.getAmount());
        } else if (Objects.equals(DepositOperationType.decr.getCode(), updateUserDepositBO.getOperationType())) {
            iUserService.decrDeposit(updateUserDepositBO.getUserId(), updateUserDepositBO.getAmount());
        }

        // 保证金记录
        iDepositRecordService.adminUpdateDeposit(updateUserDepositBO.getUserId(), updateUserDepositBO.getAmount(), updateUserDepositBO.getOperationType());
    }

    @Override
    public void updateCid(String cid) {
        LambdaUpdateWrapper<UserEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .eq(UserEntity::getUserId, UserContextHolder.getAppUserId())
                .set(UserEntity::getCid, cid);

        iUserService.update(new UserEntity(), updateWrapper);
    }
}
