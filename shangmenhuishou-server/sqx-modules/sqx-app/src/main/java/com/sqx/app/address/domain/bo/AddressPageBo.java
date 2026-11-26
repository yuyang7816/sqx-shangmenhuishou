package com.sqx.app.address.domain.bo;

import com.sqx.common.mybatis.domain.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-17 13:54:22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AddressPageBo extends PageParam {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "是否默认 1=默认 2=非默认")
    private Integer isDefault;
}
