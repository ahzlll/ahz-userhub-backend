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
 *
 * @author ahz
 * @version 1.1
 */
@Tag(name = "管理员管理", description = "管理员用户管理接口，需要管理员权限")
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private UserService userService;

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return
     */
    @Operation(summary = "删除用户", description = "管理员删除用户接口，需要在请求头中携带 Authorization，且用户角色为管理员")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/deleteUser")
    public Result<Boolean> deleteUser(
            @Parameter(description = "用户ID", required = true) @RequestBody Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户ID无效");
        }
        boolean result = userService.removeById(id);
        return ResultUtils.success(result);
    }

    /**
     * 更新用户信息
     *
     * @param updateRequest 更新请求
     * @return
     */
    @Operation(summary = "更新用户信息", description = "管理员更新用户信息接口，需要在请求头中携带 Authorization，且用户角色为管理员")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/updateUser")
    public Result<Boolean> updateUser(@RequestBody AdminUpdateUserRequest updateRequest) {
        if (updateRequest == null || updateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误");
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
     * 获取用户详情
     *
     * @param id 用户ID
     * @return
     */
    @Operation(summary = "获取用户详情", description = "管理员获取用户详情接口，需要在请求头中携带 Authorization，且用户角色为管理员")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/getUserById")
    public Result<UserDTO> getUserById(
            @Parameter(description = "用户ID", required = true) @RequestParam Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户ID无效");
        }
        User user = userService.getById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        User safetyUser = userService.getSafetyUser(user);
        UserDTO userDTO = UserConvertor.toDTO(safetyUser);
        return ResultUtils.success(userDTO);
    }

    /**
     * 获取用户列表
     *
     * @param current 当前页
     * @param pageSize 每页大小
     * @param username 用户名（可选，用于搜索）
     * @return
     */
    @Operation(summary = "获取用户列表", description = "管理员获取用户列表接口，支持分页和搜索，需要在请求头中携带 Authorization，且用户角色为管理员")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/getUserList")
    public Result<Page<UserDTO>> getUserList(
            @Parameter(description = "当前页", required = false) @RequestParam(defaultValue = "1") Long current,
            @Parameter(description = "每页大小", required = false) @RequestParam(defaultValue = "10") Long pageSize,
            @Parameter(description = "用户名（支持模糊搜索）", required = false) @RequestParam(required = false) String username) {
        Page<User> page = new Page<>(current, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        Page<User> userPage = userService.page(page, queryWrapper);
        
        // 转换为 DTO 并脱敏
        Page<UserDTO> dtoPage = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        List<UserDTO> dtoList = userPage.getRecords().stream()
                .map(user -> {
                    User safetyUser = userService.getSafetyUser(user);
                    return UserConvertor.toDTO(safetyUser);
                })
                .collect(Collectors.toList());
        dtoPage.setRecords(dtoList);
        
        return ResultUtils.success(dtoPage);
    }
}

