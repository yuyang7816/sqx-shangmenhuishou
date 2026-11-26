package com.sqx.app.authentication.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户实名认证
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-01 14:08:36
 */
@Getter
@Setter
@TableName("user_authentication")
@Schema(name = "UserAuthentication", description = "用户实名认证")
public class UserAuthentication implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "真实姓名")
    @TableField("name")
    private String name;

    @Schema(description = "身份证号")
    @TableField("id_card")
    private String idCard;

    @Schema(description = "性别 1=男 2=女")
    @TableField("gender")
    private Integer gender;

    @Schema(description = "联系方式")
    @TableField("contact")
    private String contact;

    @Schema(description = "身份证正面")
    @TableField("id_card_front")
    private String idCardFront;

    @Schema(description = "身份证反面")
    @TableField("id_card_back")
    private String idCardBack;

    @Schema(description = "证书")
    @TableField("credential")
    private String credential;

    @Schema(description = "状态 1=待审核 2=通过 3=拒绝")
    @TableField("status")
    private Integer status;

    @Schema(description = "审核理由")
    @TableField("reason")
    private String reason;

    @Schema(description = "出生日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

}
