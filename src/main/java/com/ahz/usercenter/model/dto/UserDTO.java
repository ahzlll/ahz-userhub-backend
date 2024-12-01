package com.ahz.usercenter.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户数据传输对象（脱敏后的用户信息）
 * 用于在 Token 中存储，不包含敏感信息如密码
 *
 * @author ahz
 * @version 2.0
 */
@Data
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户 id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 性别
     */
    private String gender;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户状态: active, inactive, banned
     */
    private String userStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 用户角色: user, admin
     */
    private String userRole;
}

