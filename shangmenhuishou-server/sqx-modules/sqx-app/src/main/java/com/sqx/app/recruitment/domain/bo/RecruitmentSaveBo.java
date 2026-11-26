package com.sqx.app.recruitment.domain.bo;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-17 15:30:33
 */
@Data
public class RecruitmentSaveBo {

    @Schema(description = "用户id")
    @NotNull(message = "用户id不能为空")
    private Long userId;

    @Schema(description = "城市")
    @NotBlank(message = "城市不能为空")
    private String city;

    @Schema(description = "姓名")
    @NotBlank(message = "姓名不能为空")
    private String name;

    @Schema(description = "联系电话")
    @NotBlank(message = "联系电话不能为空")
    private String phone;

    @Schema(description = "年龄")
    @NotNull(message = "年龄不能为空")
    private Integer age;

    @Schema(description = "身份证正面")
    @NotBlank(message = "身份证正面不能为空")
    private String idCardFront;

    @Schema(description = "身份证反面")
    @NotBlank(message = "身份证反面不能为空")
    private String idCardBack;

}
