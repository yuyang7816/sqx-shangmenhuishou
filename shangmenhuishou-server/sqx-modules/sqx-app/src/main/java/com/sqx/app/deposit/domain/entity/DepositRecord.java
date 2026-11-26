package com.sqx.app.deposit.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 保证金记录
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-10-13 14:47:55
 */
@Getter
@Setter
@TableName("deposit_record")
@Schema(name = "DepositRecord", description = "保证金记录")
public class DepositRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户id")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "标题")
    @TableField("title")
    private String title;

    @Schema(description = "内容")
    @TableField("content")
    private String content;

    @Schema(description = "金额")
    @TableField("amount")
    private BigDecimal amount;

    @Schema(description = "资金流向: 1=收入, 2=扣除")
    @TableField("direction")
    private Integer direction;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

}
