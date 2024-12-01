package com.ahz.usercenter.model.domain.response;

import com.ahz.usercenter.model.dto.UserDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * 登录响应类
 * 包含 Token 和用户信息
 *
 * @author ahz
 * @version 2.0
 */
@Data
public class LoginResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Token
     */
    private String token;

    /**
     * 用户信息（脱敏后的）
     */
    private UserDTO user;

    public LoginResponse() {
    }

    public LoginResponse(String token, UserDTO user) {
        this.token = token;
        this.user = user;
    }
}

