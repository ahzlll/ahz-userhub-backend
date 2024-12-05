package com.ahz.usercenter.controller;

import com.ahz.usercenter.common.Result;
import com.ahz.usercenter.common.ErrorCode;
import com.ahz.usercenter.common.ResultUtils;
import com.ahz.usercenter.exception.BusinessException;
import com.ahz.usercenter.model.domain.User;
import com.ahz.usercenter.model.domain.request.UserUpdateRequest;
import com.ahz.usercenter.model.dto.UserDTO;
import com.ahz.usercenter.service.UserService;
import com.ahz.usercenter.utils.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

/**
 * 用户接口
 * 处理当前登录用户相关的操作
 *
 * @author ahz
 * @version 3.0
 */
@Tag(name = "用户接口", description = "当前用户信息管理接口")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 获取当前用户信息
     * 从 UserContext（ThreadLocal）中获取，由拦截器设置
     *
     * @return 当前用户信息
     */
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户信息，需要在请求头中携带 Authorization: Bearer <token>")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/me")
    public Result<UserDTO> getCurrentUser() {
        UserDTO currentUser = UserContext.get();
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        return ResultUtils.success(currentUser);
    }

    /**
     * 更新当前用户信息（部分更新）
     *
     * @param updateRequest 更新请求
     * @return 操作结果
     */
    @Operation(summary = "更新当前用户信息", description = "更新当前登录用户信息（部分更新），需要在请求头中携带 Authorization: Bearer <token>")
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/me")
    public Result<Boolean> updateCurrentUser(@RequestBody UserUpdateRequest updateRequest) {
        if (updateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        // 获取当前登录用户
        UserDTO currentUser = UserContext.get();
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        // 更新用户信息
        User user = new User();
        user.setId(currentUser.getId());
        if (StringUtils.isNotBlank(updateRequest.getUsername())) {
            user.setUsername(updateRequest.getUsername());
        }
        if (StringUtils.isNotBlank(updateRequest.getAvatarUrl())) {
            user.setAvatarUrl(updateRequest.getAvatarUrl());
        }
        if (updateRequest.getGender() != null) {
            user.setGender(updateRequest.getGender());
        }
        if (StringUtils.isNotBlank(updateRequest.getPhone())) {
            user.setPhone(updateRequest.getPhone());
        }
        if (StringUtils.isNotBlank(updateRequest.getEmail())) {
            user.setEmail(updateRequest.getEmail());
        }
        boolean result = userService.updateById(user);
        return ResultUtils.success(result);
    }
}
