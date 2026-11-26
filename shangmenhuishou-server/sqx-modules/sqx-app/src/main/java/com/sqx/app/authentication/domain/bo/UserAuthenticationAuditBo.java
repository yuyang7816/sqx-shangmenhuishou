package com.sqx.app.authentication.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * @author chengming
 * @since 2025/9/1
 */
@Data
public class UserAuthenticationAuditBo {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "状态 1=待审核 2=通过 3=拒绝")
    @Range(min = 2, max = 3, message = "状态只能为2或着3")
    private Integer status;

    @Schema(description = "审核理由")
    private String reason;
}
