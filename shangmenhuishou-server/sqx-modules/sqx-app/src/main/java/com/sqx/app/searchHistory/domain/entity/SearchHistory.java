package com.sqx.app.searchHistory.domain.entity;

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
 * 用户搜索历史
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-17 13:23:01
 */
@Getter
@Setter
@TableName("search_history")
@Schema(name = "SearchHistory", description = "用户搜索历史")
public class SearchHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(description = "id")
            @TableId(value = "id", type = IdType.AUTO)
                private Long id;

    @Schema(description = "用户id")
    @TableField("user_id")
        private Long userId;

    @Schema(description = "搜索内容")
    @TableField("keyword")
        private String keyword;

    @Schema(description = "创建时间")
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
        private LocalDateTime createTime;

    @Schema(description = "修改时间")
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
        private LocalDateTime updateTime;

}
