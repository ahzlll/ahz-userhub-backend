package com.ahz.usercenter.model.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 管理员更新用户请求体
 *
 * @author ahz
 * @version 1.0
 */
@Data
@Schema(description = "管理员更新用户请求（id 必填，其他字段可选）")
public class AdminUpdateUserRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称", example = "管理员修改的昵称")
    private String username;

    /**
     * 用户头像
     */
    @Schema(description = "用户头像URL", example = "https://example.com/avatar.jpg")
    private String avatarUrl;

    /**
     * 性别
     */
    @Schema(description = "性别（male/female/unknown）", example = "unknown")
    private String gender;

    /**
     * 电话
     */
    @Schema(description = "电话号码", example = "1xxxxxxxxxx")
    private String phone;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱地址", example = "admin@example.com")
    private String email;

    /**
     * 用户状态: active, inactive, banned
     */
    @Schema(description = "用户状态（active/inactive/banned）", example = "active")
    private String userStatus;

    /**
     * 用户角色: user, admin
     */
    @Schema(description = "用户角色（user/admin）", example = "user")
    private String userRole;
}

