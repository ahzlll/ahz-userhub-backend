package com.ahz.usercenter.utils;

import com.ahz.usercenter.model.dto.UserDTO;

/**
 * 用户上下文工具类
 * 使用 ThreadLocal 保存当前登录用户信息，方便在 Service 层获取
 *
 * @author ahz
 * @version 2.0
 */
public class UserContext {

    private static final ThreadLocal<UserDTO> userHolder = new ThreadLocal<>();

    /**
     * 设置当前用户
     *
     * @param user 用户信息
     */
    public static void set(UserDTO user) {
        userHolder.set(user);
    }

    /**
     * 获取当前用户
     *
     * @return 用户信息
     */
    public static UserDTO get() {
        return userHolder.get();
    }

    /**
     * 获取当前用户 ID
     *
     * @return 用户 ID
     */
    public static Long getUserId() {
        UserDTO user = userHolder.get();
        return user != null ? user.getId() : null;
    }

    /**
     * 清除当前用户信息
     * 注意：在请求结束后必须调用，避免内存泄漏
     */
    public static void clear() {
        userHolder.remove();
    }
}

