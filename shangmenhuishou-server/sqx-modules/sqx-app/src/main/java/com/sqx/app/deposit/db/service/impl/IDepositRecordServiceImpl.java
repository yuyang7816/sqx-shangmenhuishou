package com.sqx.app.deposit.db.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.app.base.enums.DepositOperationType;
import com.sqx.app.deposit.db.mapper.DepositRecordMapper;
import com.sqx.app.deposit.db.service.IDepositRecordService;
import com.sqx.app.deposit.domain.bo.DepositRecordPageBo;
import com.sqx.app.deposit.domain.entity.DepositRecord;
import com.sqx.app.deposit.domain.vo.DepositRecordVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * <p>
 * 保证金记录 服务实现类
 * </p>
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-10-13 14:47:55
 */
@Service
public class IDepositRecordServiceImpl extends ServiceImpl<DepositRecordMapper, DepositRecord> implements IDepositRecordService {

    @Resource
    private DepositRecordMapper depositRecordMapper;

    @Override
    public void saveDeposit(Long userId, BigDecimal amount) {
        DepositRecord depositRecord = new DepositRecord();
        depositRecord.setUserId(userId);
        depositRecord.setAmount(amount);
        depositRecord.setTitle("缴纳保证金");
        depositRecord.setContent(String.format("缴纳保证金: [%s]", amount.toString()));
        depositRecord.setDirection(1);

        this.save(depositRecord);
    }

    @Override
    public IPage<DepositRecordVo> pageInfo(Page<DepositRecord> initPage, DepositRecordPageBo depositRecordPageBo) {
        return depositRecordMapper.pageInfo(initPage, depositRecordPageBo);
    }

    @Override
    public void adminUpdateDeposit(Long userId, BigDecimal amount, String operationType) {
        DepositRecord depositRecord = new DepositRecord();
        depositRecord.setUserId(userId);
        depositRecord.setAmount(amount);
        depositRecord.setTitle("管理员修改保证金");
        depositRecord.setContent(String.format("管理员修改保证金: [%s]", amount.toString()));

        Integer direction = getDirection(operationType);
        depositRecord.setDirection(direction);

        this.save(depositRecord);
    }

    @Override
    public void refund(Long userId, BigDecimal amount) {
        DepositRecord depositRecord = new DepositRecord();
        depositRecord.setUserId(userId);
        depositRecord.setAmount(amount);
        depositRecord.setTitle("退保证金");
        depositRecord.setContent(String.format("退保证金: [%s]", amount.toString()));
        depositRecord.setDirection(2);

        this.save(depositRecord);
    }

    private Integer getDirection(String operationType) {
        return Objects.equals(DepositOperationType.incr.getCode(), operationType) ? 1 : 2;
    }
}
