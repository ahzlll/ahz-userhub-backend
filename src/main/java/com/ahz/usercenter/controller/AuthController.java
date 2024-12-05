package com.ahz.usercenter.controller;

import com.ahz.usercenter.common.Result;
import com.ahz.usercenter.common.ErrorCode;
import com.ahz.usercenter.common.ResultUtils;
import com.ahz.usercenter.exception.BusinessException;
import com.ahz.usercenter.model.domain.User;
import com.ahz.usercenter.model.domain.request.UserLoginRequest;
import com.ahz.usercenter.model.domain.request.UserRegisterRequest;
import com.ahz.usercenter.model.domain.response.LoginResponse;
import com.ahz.usercenter.model.dto.UserDTO;
import com.ahz.usercenter.service.UserService;
import com.ahz.usercenter.utils.TokenUtils;
import com.ahz.usercenter.utils.UserConvertor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 认证接口
 * 处理用户注册、登录、注销等认证相关操作
 *
 * @author ahz
 * @version 3.0
 */
@Tag(name = "认证接口", description = "用户注册、登录、注销等认证相关接口")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Resource
    private UserService userService;

    @Resource
    private TokenUtils tokenUtils;

    /**
     * 用户注册
     *
     * @param userRegisterRequest 注册请求
     * @return 用户ID
     */
    @Operation(summary = "用户注册", description = "新用户注册接口，需要提供账号、密码和确认密码")
    @PostMapping("/register")
    public Result<Long> register(@RequestBody UserRegisterRequest userRegisterRequest) {
        // 校验
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest 登录请求
     * @param request HTTP 请求
     * @return Token 和用户信息
     */
    @Operation(summary = "用户登录", description = "用户登录接口，返回 Token 和用户信息")
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 1. 验证登录
        User user = userService.userLogin(userAccount, userPassword, request);
        // 2. 转换为 DTO（脱敏）
        UserDTO userDTO = UserConvertor.toDTO(user);
        if (userDTO == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "用户信息转换失败");
        }
        // 3. 生成 Token
        String token = tokenUtils.generateToken(user.getId());
        if (token == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Token 生成失败");
        }
        // 4. 存储 Token 到 Redis
        tokenUtils.storeToken(token, userDTO);
        // 5. 返回 Token 和用户信息
        LoginResponse loginResponse = new LoginResponse(token, userDTO);
        return ResultUtils.success(loginResponse);
    }

    /**
     * 用户注销
     *
     * @param request HTTP 请求
     * @return 操作结果
     */
    @Operation(summary = "退出登录", description = "用户注销接口，清除 Redis 中的 Token，需要在请求头中携带 Authorization")
    @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/logout")
    public Result<Integer> logout(HttpServletRequest request) {
        // 从请求头获取 Token
        String token = request.getHeader("Authorization");
        if (StringUtils.isNotBlank(token)) {
            // 删除 Redis 中的 Token
            @SuppressWarnings("null")
            String nonNullToken = token;
            tokenUtils.deleteToken(nonNullToken);
        }
        return ResultUtils.success(1);
    }
}

