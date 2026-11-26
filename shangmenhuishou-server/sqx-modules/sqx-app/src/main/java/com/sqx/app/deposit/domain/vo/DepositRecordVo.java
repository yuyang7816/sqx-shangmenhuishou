package com.sqx.app.deposit.domain.vo;

import com.sqx.app.deposit.domain.entity.DepositRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-10-13 14:47:55
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DepositRecordVo extends DepositRecord{

    @Schema(description = "用户名称")
    private String userName;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "头像")
    private String avatar;
}
