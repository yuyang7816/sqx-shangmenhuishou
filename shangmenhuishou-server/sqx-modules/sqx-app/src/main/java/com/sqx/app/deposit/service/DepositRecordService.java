package com.sqx.app.deposit.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.deposit.domain.bo.DepositRecordInfoBo;
import com.sqx.app.deposit.domain.bo.DepositRecordPageBo;
import com.sqx.app.deposit.domain.bo.DepositRecordSaveBo;
import com.sqx.app.deposit.domain.bo.DepositRecordUpdateBo;
import com.sqx.app.deposit.domain.vo.DepositRecordVo;

/**
 * 保证金记录 服务类
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-10-13 14:47:55
 */
public interface DepositRecordService {

    void saveInfo(DepositRecordSaveBo depositRecordSaveBo);

    IPage<DepositRecordVo> pageInfo(DepositRecordPageBo depositRecordPageBo);

    DepositRecordVo info(DepositRecordInfoBo depositRecordInfoBo);

    void updateInfo(DepositRecordUpdateBo depositRecordUpdateBo);

    void delete(DepositRecordInfoBo depositRecordInfoBo);
}
