package com.sqx.domain.db.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 *
 *
 * @author sqx
 * @since 2025-05-19 15:35:07
 */
@Getter
@Setter
@TableName("message_info")
@Schema(name = "MessageInfo", description = "")
public class MessageInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(description = "消息id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "标题")
    @TableField("title")
    private String title;

    @Schema(description = "内容")
    @TableField("content")
    private String content;

    @Schema(description = "图片 逗号分隔")
    @TableField("image")
    private String image;

    @Schema(description = "是否查看 1已查看 2未查看")
    @TableField("is_see")
    private Integer isSee;

    @Schema(description = "地址")
    @TableField("url")
    private String url;

    @Schema(description = "类型 1系统公告 2用户消息")
    @TableField("type")
    private Integer type;

    @Schema(description = "用户id")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "服务id")
    @TableField("data_id")
    private Long dataId;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

}
