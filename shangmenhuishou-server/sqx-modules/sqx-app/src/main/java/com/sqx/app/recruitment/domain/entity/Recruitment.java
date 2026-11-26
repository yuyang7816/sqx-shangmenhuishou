package com.sqx.app.recruitment.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 招募
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-17 15:30:33
 */
@Getter
@Setter
@TableName("recruitment")
@Schema(name = "Recruitment", description = "招募")
public class Recruitment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户id")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "城市")
    @TableField("city")
    private String city;

    @Schema(description = "姓名")
    @TableField("name")
    private String name;

    @Schema(description = "联系电话")
    @TableField("phone")
    private String phone;

    @Schema(description = "年龄")
    @TableField("age")
    private Integer age;

    @Schema(description = "身份证正面")
    @TableField("id_card_front")
    private String idCardFront;

    @Schema(description = "身份证反面")
    @TableField("id_card_back")
    private String idCardBack;

    @Schema(description = "状态 0=待处理 1=通过 2=拒绝")
    @TableField("status")
    private Integer status;

    @Schema(description = "审核理由")
    @TableField("reason")
    private String reason;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

}
