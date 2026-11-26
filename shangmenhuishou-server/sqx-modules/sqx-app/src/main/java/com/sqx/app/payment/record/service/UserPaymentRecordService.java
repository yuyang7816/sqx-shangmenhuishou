package com.sqx.app.payment.record.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.payment.record.domain.bo.UserPaymentRecordInfoBo;
import com.sqx.app.payment.record.domain.bo.UserPaymentRecordPageBo;
import com.sqx.app.payment.record.domain.bo.UserPaymentRecordSaveBo;
import com.sqx.app.payment.record.domain.bo.UserPaymentRecordUpdateBo;
import com.sqx.app.payment.record.domain.vo.UserPaymentRecordVo;

/**
 * 用户支付记录表 服务类
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-08-13 15:19:30
 */
public interface UserPaymentRecordService {

    void saveInfo(UserPaymentRecordSaveBo userPaymentRecordSaveBo);

    IPage<UserPaymentRecordVo> pageInfo(UserPaymentRecordPageBo userPaymentRecordPageBo);

    UserPaymentRecordVo info(UserPaymentRecordInfoBo userPaymentRecordInfoBo);

    void updateInfo(UserPaymentRecordUpdateBo userPaymentRecordUpdateBo);

    void delete(UserPaymentRecordInfoBo userPaymentRecordInfoBo);
}
