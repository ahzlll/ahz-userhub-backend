package com.ahz.usercenter.interceptor;

import com.ahz.usercenter.common.ErrorCode;
import com.ahz.usercenter.exception.BusinessException;
import com.ahz.usercenter.model.dto.UserDTO;
import com.ahz.usercenter.utils.TokenUtils;
import com.ahz.usercenter.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 登录拦截器
 * 用于验证 Token 和权限控制
 *
 * @author ahz
 * @version 2.0
 */
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    private TokenUtils tokenUtils;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {
        // 1. 从请求头获取 Token
        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)) {
            log.warn("Token is missing in request header");
            throw new BusinessException(ErrorCode.NOT_LOGIN, "未登录");
        }
        // token 已通过 StringUtils.isBlank 检查，不为 null
        // 2. 验证 Token
        @SuppressWarnings("null")
        UserDTO user = tokenUtils.verifyToken(token);
        if (user == null) {
            log.warn("Token verification failed: {}", token);
            throw new BusinessException(ErrorCode.NOT_LOGIN, "登录已过期");
        }

        // 3. 权限校验（管理员接口需要 admin 角色）
        String requestURI = request.getRequestURI();
        // 检查是否是管理员接口（/api/admin/* 需要管理员权限）
        if (requestURI.contains("/admin/")) {
            if (user.getUserRole() == null || !"admin".equals(user.getUserRole())) {
                log.warn("User {} attempted to access admin resource without permission", user.getId());
                throw new BusinessException(ErrorCode.NO_AUTH, "无权限");
            }
        }

        // 4. 保存用户信息到 ThreadLocal，方便后续获取
        UserContext.set(user);
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull Object handler,
                                @Nullable Exception ex) throws Exception {
        // 请求结束后清除 ThreadLocal，避免内存泄漏
        UserContext.clear();
    }
}

