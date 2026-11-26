package com.sqx.app.base.app.domain.bo;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-11-18 15:55:50
 */
@Data
public class AppSaveBo {

    @Schema(description = "")
    private Long id;

    @Schema(description = "")
    private String androidWgtUrl;

    @Schema(description = "")
    private String createAt;

    @Schema(description = "")
    private String des;

    @Schema(description = "")
    private String iosVersion;

    @Schema(description = "")
    private String iosWgtUrl;

    @Schema(description = "")
    private String method;

    @Schema(description = "")
    private String version;

    @Schema(description = "")
    private String wgtUrl;

}
