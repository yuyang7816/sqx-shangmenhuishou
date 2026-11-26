package com.sqx.app.deposit.db.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.app.deposit.domain.bo.DepositRecordPageBo;
import com.sqx.app.deposit.domain.entity.DepositRecord;
import com.sqx.app.deposit.domain.vo.DepositRecordVo;

import java.math.BigDecimal;

/**
 * <p>
 * 保证金记录 服务类
 * </p>
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-10-13 14:47:55
 */
public interface IDepositRecordService extends IService<DepositRecord> {

    /**
     * 保存缴纳保证金
     *
     * @param userId 用户id
     * @param amount 保证金金额
     */
    void saveDeposit(Long userId, BigDecimal amount);

    IPage<DepositRecordVo> pageInfo(Page<DepositRecord> initPage, DepositRecordPageBo depositRecordPageBo);

    void adminUpdateDeposit(Long userId, BigDecimal amount, String operationType);

    /**
     * 退保证金
     * @param userId 用户id
     * @param amount 保证金金额
     */
    void refund(Long userId, BigDecimal amount);
}
