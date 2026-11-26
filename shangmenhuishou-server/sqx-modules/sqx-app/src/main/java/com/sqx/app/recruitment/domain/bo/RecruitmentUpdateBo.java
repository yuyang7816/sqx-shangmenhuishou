package com.sqx.app.recruitment.domain.bo;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

/**
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-17 15:30:33
 */
@Data
public class RecruitmentUpdateBo {

    @Schema(description = "id")
    @NotNull(message = "id不能为空")
    private Long id;
    @Schema(description = "用户id")
    private Long userId;
    @Schema(description = "城市")
    private String city;
    @Schema(description = "姓名")
    private String name;
    @Schema(description = "联系电话")
    private String phone;
    @Schema(description = "年龄")
    private Integer age;
    @Schema(description = "身份证正面")
    private String idCardFront;
    @Schema(description = "身份证反面")
    private String idCardBack;
}
