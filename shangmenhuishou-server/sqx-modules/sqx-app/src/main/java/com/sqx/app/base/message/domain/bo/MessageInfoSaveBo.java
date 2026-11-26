package com.sqx.app.base.message.domain.bo;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * @author sqx
 * @since 2025-05-19 15:35:07
 */
@Data
public class MessageInfoSaveBo {

    @Schema(description = "消息id")
    private Long id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "图片 逗号分隔")
    private String image;

    @Schema(description = "是否查看 1已查看 2未查看")
    private Boolean isSee;

    @Schema(description = "地址")
    private String url;

    @Schema(description = "类型 1系统公告 2用户消息")
    private Byte type;

    @Schema(description = "用户id")
    private Integer userId;

    @Schema(description = "服务id")
    private Integer dataId;

}
