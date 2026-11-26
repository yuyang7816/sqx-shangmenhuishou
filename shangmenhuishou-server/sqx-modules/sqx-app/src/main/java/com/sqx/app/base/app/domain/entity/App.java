package com.sqx.app.base.app.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * app升级
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-11-18 15:55:50
 */
@Getter
@Setter
@TableName("app")
@Schema(name = "App", description = "app升级")
public class App implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("android_wgt_url")
    private String androidWgtUrl;

    @TableField("create_at")
    private String createAt;

    @TableField("des")
    private String des;

    @TableField("ios_version")
    private String iosVersion;

    @TableField("ios_wgt_url")
    private String iosWgtUrl;

    @TableField("method")
    private String method;

    @TableField("version")
    private String version;

    @TableField("wgt_url")
    private String wgtUrl;

}
