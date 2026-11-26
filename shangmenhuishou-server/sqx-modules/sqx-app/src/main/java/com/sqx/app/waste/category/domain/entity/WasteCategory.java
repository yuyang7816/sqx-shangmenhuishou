package com.sqx.app.waste.category.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 废品类别
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-03 11:36:52
 */
@Getter
@Setter
@TableName("waste_category")
@Schema(name = "WasteCategory", description = "废品类别")
public class WasteCategory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "父类别ID。顶级大类为0")
    @TableField("parent_id")
    private Long parentId;

    @Schema(description = "类别名称")
    @TableField("name")
    private String name;

    @Schema(description = "层级 (1: 大类, 2: 子类)")
    @TableField("level")
    private Integer level;

    @Schema(description = "封面图")
    private String cover;

    @Schema(description = "详情图 逗号分割")
    private String pic;

    @Schema(description = "标签 逗号分割")
    private String tags;

    @Schema(description = "预约次数")
    private Integer appointmentNum;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

}
