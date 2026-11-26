package com.sqx.app.authentication.domain.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-01 14:08:36
 */
@Data
public class UserAuthenticationUpdateBo {

    @Schema(description = "id")
    private Long id;
    @Schema(description = "真实姓名")
    private String name;
    @Schema(description = "身份证号")
    private String idCard;
    @Schema(description = "性别 1=男 2=女")
    private Integer gender;
    @Schema(description = "联系方式")
    private String contact;
    @Schema(description = "身份证正面")
    private String idCardFront;
    @Schema(description = "身份证反面")
    private String idCardBack;
    @Schema(description = "证书")
    private String credential;
    @Schema(description = "状态 1=待审核 2=通过 3=拒绝")
    private Integer status;
    @Schema(description = "审核理由")
    private String reason;
    @Schema(description = "出生日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
}
