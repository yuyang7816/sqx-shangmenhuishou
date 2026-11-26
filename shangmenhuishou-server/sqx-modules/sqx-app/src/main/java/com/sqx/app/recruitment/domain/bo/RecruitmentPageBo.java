package com.sqx.app.recruitment.domain.bo;

import com.sqx.common.mybatis.domain.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-17 15:30:33
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RecruitmentPageBo extends PageParam {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "城市")
    private String city;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "状态 0=待处理 1=通过 2=拒绝")
    private Integer status;
}
