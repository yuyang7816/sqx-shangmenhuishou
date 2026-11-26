package com.sqx.app.messages.domain.bo;

import com.sqx.common.mybatis.domain.PageParam ;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-08-15 16:28:04
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MessagesPageBo extends PageParam {

    @Schema(description = "用户id", hidden = true)
    private Long userId;

    @Schema(description = "类型 1系统公告 2用户消息")
    private Integer type;
}
