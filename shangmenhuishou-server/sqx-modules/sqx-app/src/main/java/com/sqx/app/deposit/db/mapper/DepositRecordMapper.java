package com.sqx.app.deposit.db.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.app.deposit.domain.bo.DepositRecordPageBo;
import com.sqx.app.deposit.domain.entity.DepositRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.app.deposit.domain.vo.DepositRecordVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 保证金记录 Mapper 接口
 * </p>
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-10-13 14:47:55
 */
public interface DepositRecordMapper extends BaseMapper<DepositRecord> {

    IPage<DepositRecordVo> pageInfo(Page<DepositRecord> initPage, @Param("param") DepositRecordPageBo depositRecordPageBo);
}
