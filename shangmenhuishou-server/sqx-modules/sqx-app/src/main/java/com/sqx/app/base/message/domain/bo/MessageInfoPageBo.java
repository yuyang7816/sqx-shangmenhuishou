package com.sqx.app.base.message.domain.bo;

import com.sqx.common.mybatis.domain.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author sqx
 * @since 2025-05-19 15:35:07
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MessageInfoPageBo extends PageParam {

    @Schema(description = "类型 1系统公告 2用户消息")
    private Integer type;
}
