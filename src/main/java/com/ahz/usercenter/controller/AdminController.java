package com.ahz.usercenter.controller;

import com.ahz.usercenter.common.Result;
import com.ahz.usercenter.common.ErrorCode;
import com.ahz.usercenter.common.ResultUtils;
import com.ahz.usercenter.exception.BusinessException;
import com.ahz.usercenter.model.domain.User;
import com.ahz.usercenter.model.domain.request.AdminUpdateUserRequest;
import com.ahz.usercenter.model.dto.UserDTO;
import com.ahz.usercenter.service.UserService;
import com.ahz.usercenter.utils.UserConvertor;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 管理员接口
 * 处理管理员对用户的管理操作
 *
 * @author ahz
 * @version 3.0
 */
@Tag(name = "管理员管理", description = "管理员用户管理接口，需要管理员权限")
@RestController
@RequestMapping("/api/v1/users")
public class AdminController {

    @Resource
    private UserService userService;

    /**
     * 分页查询用户列表
     *
     * @param page 当前页（默认1）
     * @param size 每页大小（默认10）
     * @param username 用户名（可选，用于模糊搜索）
     * @param role 用户角色（可选，用于过滤）
     * @param status 用户状态（可选，用于过滤）
     * @return 用户列表
     */
    @Operation(summary = "分页查询用户列表", description = "管理员获取用户列表接口，支持分页、搜索和过滤，需要在请求头中携带 Authorization: Bearer <token>，且用户角色为管理员")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping
    public Result<Page<UserDTO>> getUserList(
            @Parameter(description = "当前页", required = false) @RequestParam(defaultValue = "1") Long page,
            @Parameter(description = "每页大小", required = false) @RequestParam(defaultValue = "10") Long size,
            @Parameter(description = "用户名（支持模糊搜索）", required = false) @RequestParam(required = false) String username,
            @Parameter(description = "用户角色（用于过滤）", required = false) @RequestParam(required = false) String role,
            @Parameter(description = "用户状态（用于过滤）", required = false) @RequestParam(required = false) Integer status) {
        Page<User> userPage = new Page<>(page, size);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        if (StringUtils.isNotBlank(role)) {
            queryWrapper.eq("userRole", role);
        }
        if (status != null) {
            queryWrapper.eq("userStatus", status);
        }
        Page<User> resultPage = userService.page(userPage, queryWrapper);
        
        // 转换为 DTO 并脱敏
        Page<UserDTO> dtoPage = new Page<>(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal());
        List<UserDTO> dtoList = resultPage.getRecords().stream()
                .map(user -> {
                    User safetyUser = userService.getSafetyUser(user);
                    return UserConvertor.toDTO(safetyUser);
                })
                .collect(Collectors.toList());
        dtoPage.setRecords(dtoList);
        
        return ResultUtils.success(dtoPage);
    }

    /**
     * 获取单个用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    @Operation(summary = "获取用户信息", description = "管理员获取指定用户信息，需要在请求头中携带 Authorization: Bearer <token>，且用户角色为管理员")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{userId}")
    public Result<UserDTO> getUserById(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId) {
        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户ID无效");
        }
        User user = userService.getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        User safetyUser = userService.getSafetyUser(user);
        UserDTO userDTO = UserConvertor.toDTO(safetyUser);
        return ResultUtils.success(userDTO);
    }

    /**
     * 全量更新用户信息
     *
     * @param userId 用户ID
     * @param updateRequest 更新请求（包含所有字段）
     * @return 操作结果
     */
    @Operation(summary = "全量更新用户信息", description = "管理员全量更新用户信息，需要在请求头中携带 Authorization: Bearer <token>，且用户角色为管理员")
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/{userId}")
    public Result<Boolean> updateUser(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId,
            @RequestBody AdminUpdateUserRequest updateRequest) {
        if (updateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户ID无效");
        }
        // 确保路径中的 userId 和请求体中的 id 一致
        if (!userId.equals(updateRequest.getId())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "路径中的用户ID与请求体中的用户ID不一致");
        }
        User user = new User();
        user.setId(updateRequest.getId());
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
        if (updateRequest.getUserStatus() != null) {
            user.setUserStatus(updateRequest.getUserStatus());
        }
        if (updateRequest.getUserRole() != null) {
            user.setUserRole(updateRequest.getUserRole());
        }
        boolean result = userService.updateById(user);
        return ResultUtils.success(result);
    }

    /**
     * 部分更新用户信息
     *
     * @param userId 用户ID
     * @param updateRequest 更新请求（只包含需要更新的字段）
     * @return 操作结果
     */
    @Operation(summary = "部分更新用户信息", description = "管理员部分更新用户信息，只需要提供需要更新的字段，需要在请求头中携带 Authorization: Bearer <token>，且用户角色为管理员")
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/{userId}")
    public Result<Boolean> patchUser(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId,
            @RequestBody AdminUpdateUserRequest updateRequest) {
        if (updateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户ID无效");
        }
        // 确保路径中的 userId 和请求体中的 id 一致
        if (updateRequest.getId() != null && !userId.equals(updateRequest.getId())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "路径中的用户ID与请求体中的用户ID不一致");
        }
        User user = new User();
        user.setId(userId);
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
        if (updateRequest.getUserStatus() != null) {
            user.setUserStatus(updateRequest.getUserStatus());
        }
        if (updateRequest.getUserRole() != null) {
            user.setUserRole(updateRequest.getUserRole());
        }
        boolean result = userService.updateById(user);
        return ResultUtils.success(result);
    }

    /**
     * 删除用户（逻辑删除）
     *
     * @param userId 用户ID
     * @return 操作结果
     */
    @Operation(summary = "删除用户", description = "管理员删除用户（逻辑删除），需要在请求头中携带 Authorization: Bearer <token>，且用户角色为管理员")
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{userId}")
    public Result<Boolean> deleteUser(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId) {
        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户ID无效");
        }
        boolean result = userService.removeById(userId);
        return ResultUtils.success(result);
    }
}

