package com.sqx.app.referral.withdraw.db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.app.referral.withdraw.domain.entity.WithdrawRecord;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 提现记录 Mapper 接口
 * </p>
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-08-05 10:10:32
 */
public interface WithdrawRecordMapper extends BaseMapper<WithdrawRecord> {

    BigDecimal sumAmount(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}
