package com.sqx.app.wallet.wallet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.app.base.enums.DepositOperationType;
import com.sqx.app.wallet.transaction.db.service.IUserWalletTransactionService;
import com.sqx.app.wallet.wallet.db.service.IUserWalletService;
import com.sqx.app.wallet.wallet.domain.bo.*;
import com.sqx.app.wallet.wallet.domain.entity.UserWallet;
import com.sqx.app.wallet.wallet.domain.vo.UserWalletVo;
import com.sqx.app.wallet.wallet.service.UserWalletService;
import com.sqx.common.core.exception.BizException;
import com.sqx.common.mybatis.utils.PageUtil;
import com.sqx.common.shiro.utils.UserContextHolder;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 用户钱包主表 服务实现类
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-08-13 15:09:58
 */
@Service
public class UserWalletServiceImpl implements UserWalletService {

    @Resource
    private IUserWalletService iUserWalletService;
    @Resource
    private IUserWalletTransactionService iUserWalletTransactionService;

    @Override
    public void saveInfo(UserWalletSaveBo userWalletSaveBo) {
        UserWallet userWallet = new UserWallet();
        BeanUtils.copyProperties(userWalletSaveBo, userWallet);

        iUserWalletService.save(userWallet);
    }

    @Override
    public IPage<UserWalletVo> pageInfo(UserWalletPageBo userWalletPageBo) {
        Page<UserWallet> initPage = PageUtil.initPage(userWalletPageBo.getPage(), userWalletPageBo.getLimit());

        QueryWrapper<UserWallet> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .orderByDesc(UserWallet::getCreateTime);

        IPage<UserWallet> iPage = iUserWalletService.page(initPage, queryWrapper);
        List<UserWalletVo> voList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
            for (UserWallet record : iPage.getRecords()) {
                UserWalletVo vo = new UserWalletVo();
                BeanUtils.copyProperties(record, vo);

                voList.add(vo);
            }
        }

        IPage<UserWalletVo> voIPage = PageUtil.copyPage(iPage, voList);
        return voIPage;
    }

    @Override
    public UserWalletVo info(UserWalletInfoBo userWalletInfoBo) {
        UserWallet userWallet = iUserWalletService.getById(userWalletInfoBo.getId());
        if (Objects.isNull(userWallet)) {
            throw new BizException("信息不存在");
        }

        UserWalletVo vo = new UserWalletVo();
        BeanUtils.copyProperties(userWallet, vo);
        return vo;
    }

    @Override
    public void updateInfo(UserWalletUpdateBo userWalletUpdateBo) {
        UserWallet userWallet = new UserWallet();
        BeanUtils.copyProperties(userWalletUpdateBo, userWallet);

        iUserWalletService.updateById(userWallet);
    }

    @Override
    public void delete(UserWalletInfoBo userWalletInfoBo) {
        iUserWalletService.removeById(userWalletInfoBo.getId());
    }

    @Override
    public UserWalletVo my() {
        UserWallet userWallet = iUserWalletService.getByUserId(UserContextHolder.getAppUserId());
        if (Objects.isNull(userWallet)) {
            throw new BizException("钱包不存在");
        }

        UserWalletVo vo = new UserWalletVo();
        BeanUtils.copyProperties(userWallet, vo);
        return vo;
    }

    @Override
    public void updateBalance(UpdateUserBalanceBO updateUserBalanceBO) {
        if (Objects.equals(DepositOperationType.incr.getCode(), updateUserBalanceBO.getOperationType())) {
            iUserWalletService.incr(updateUserBalanceBO.getUserId(), updateUserBalanceBO.getAmount());
        } else if (Objects.equals(DepositOperationType.decr.getCode(), updateUserBalanceBO.getOperationType())) {
            iUserWalletService.decr(updateUserBalanceBO.getUserId(), updateUserBalanceBO.getAmount());
        }

        iUserWalletTransactionService.adminUpdate(updateUserBalanceBO.getUserId(), updateUserBalanceBO.getAmount(), updateUserBalanceBO.getOperationType());
    }
}
