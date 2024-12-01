package com.ahz.usercenter.model.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求体
 *
 * @author ahz
 * @version 1.0
 */
@Data
@Schema(description = "用户登录请求")
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 用户账号
     */
    @Schema(description = "用户账号", example = "testuser", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userAccount;

    /**
     * 用户密码
     */
    @Schema(description = "用户密码", example = "12345678", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userPassword;
}
