package com.sqx.app.deposit.domain.bo;

import com.sqx.common.mybatis.domain.PageParam ;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-10-13 14:47:55
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DepositRecordPageBo extends PageParam {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "资金流向: 1=收入, 2=支出")
    private Integer direction;

    @Schema(description = "用户名称")
    private String userName;

    @Schema(description = "手机号")
    private String phone;

}
