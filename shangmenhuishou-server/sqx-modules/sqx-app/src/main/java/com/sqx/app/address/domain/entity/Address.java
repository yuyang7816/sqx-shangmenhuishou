package com.sqx.app.address.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 地址
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-17 13:54:22
 */
@Getter
@Setter
@TableName("address")
@Schema(name = "Address", description = "地址")
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "姓名")
    @TableField("name")
    private String name;

    @Schema(description = "手机号")
    @TableField("phone")
    private String phone;

    @Schema(description = "省")
    @TableField("province")
    private String province;

    @Schema(description = "市")
    @TableField("city")
    private String city;

    @Schema(description = "区")
    @TableField("district")
    private String district;

    @Schema(description = "详细地址")
    @TableField("address")
    private String address;

    @Schema(description = "是否默认 1=默认 2=非默认")
    @TableField("is_default")
    private Integer isDefault;

    @Schema(description = "纬度")
    @TableField("lat")
    private BigDecimal lat;

    @Schema(description = "经度")
    @TableField("lng")
    private BigDecimal lng;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

}
