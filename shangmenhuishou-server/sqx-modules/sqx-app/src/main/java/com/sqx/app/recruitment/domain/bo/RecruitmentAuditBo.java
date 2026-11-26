package com.sqx.app.recruitment.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author chengming
 * @since 2025/9/17
 */
@Data
public class RecruitmentAuditBo {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "状态 0=待处理 1=通过 2=拒绝")
    private Integer status;

    @Schema(description = "理由")
    private String reason;
}
