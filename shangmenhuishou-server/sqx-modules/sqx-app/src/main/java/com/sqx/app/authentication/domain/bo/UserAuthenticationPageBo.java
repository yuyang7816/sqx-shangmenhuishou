package com.sqx.app.authentication.domain.bo;

import com.sqx.common.mybatis.domain.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-01 14:08:36
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserAuthenticationPageBo extends PageParam {

    @Schema(description = "真实姓名")
    private String name;

    @Schema(description = "身份证号")
    private String idCard;

    @Schema(description = "性别 1=男 2=女")
    private Integer gender;

    @Schema(description = "联系方式")
    private String contact;

    @Schema(description = "状态 1=待审核 2=通过 3=拒绝")
    private Integer status;
}
