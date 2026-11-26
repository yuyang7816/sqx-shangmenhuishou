package com.sqx.app.messages.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author chengming
 * @since 2025/11/19
 */
@Data
public class MessageDataVo {

    @Schema(description = "消息类型: 1=系统公告, 2=用户消息 3=系统通知")
    private Integer type;

    @Schema(description = "未读数")
    private Long unreadCount;
}
