package com.ahz.usercenter.model.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户更新请求体
 *
 * @author ahz
 * @version 1.0
 */
@Data
@Schema(description = "用户更新请求（所有字段均为可选）")
public class UserUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称", example = "新昵称")
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
    @Schema(description = "邮箱地址", example = "user@example.com")
    private String email;
}

