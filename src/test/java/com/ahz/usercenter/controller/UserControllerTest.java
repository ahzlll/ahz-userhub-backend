package com.ahz.usercenter.controller;

import com.ahz.usercenter.common.ErrorCode;
import com.ahz.usercenter.common.Result;
import com.ahz.usercenter.exception.BusinessException;
import com.ahz.usercenter.model.domain.request.UserUpdateRequest;
import com.ahz.usercenter.model.dto.UserDTO;
import com.ahz.usercenter.utils.UserContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户控制器测试类
 * 测试当前用户相关的接口
 *
 * @author ahz
 * @version 3.0.1
 */
@SpringBootTest
class UserControllerTest {

    @Resource
    private UserController userController;

    /**
     * 在每个测试前设置模拟的当前用户
     */
    @BeforeEach
    void setUp() {
        UserDTO mockUser = new UserDTO();
        mockUser.setId(1L);
        mockUser.setUsername("测试用户");
        mockUser.setUserAccount("test@example.com");
        mockUser.setAvatarUrl("https://example.com/avatar.jpg");
        mockUser.setGender("unknown");
        mockUser.setPhone("12345678901");
        mockUser.setEmail("test@example.com");
        mockUser.setUserStatus("active");
        mockUser.setUserRole("user");
        UserContext.set(mockUser);
    }

    /**
     * 在每个测试后清除用户上下文，避免影响其他测试
     */
    @AfterEach
    void tearDown() {
        UserContext.clear();
    }

    /**
     * 测试获取当前用户信息 - 正常情况
     */
    @Test
    void testGetCurrentUser() {
        Result<UserDTO> result = userController.getCurrentUser();
        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
        assertEquals("test@example.com", result.getData().getUserAccount());
        assertEquals("测试用户", result.getData().getUsername());
    }

    /**
     * 测试获取当前用户信息 - 未登录
     */
    @Test
    void testGetCurrentUserWhenNotLoggedIn() {
        UserContext.clear(); // 清除用户上下文，模拟未登录状态

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userController.getCurrentUser();
        });
        assertEquals(ErrorCode.NOT_LOGIN.getCode(), exception.getCode());
    }

    /**
     * 测试更新当前用户信息 - 正常情况
     */
    @Test
    void testUpdateCurrentUser() {
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setUsername("新昵称");
        updateRequest.setAvatarUrl("https://example.com/new-avatar.jpg");

        Result<Boolean> result = userController.updateCurrentUser(updateRequest);
        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
    }

    /**
     * 测试更新当前用户信息 - 请求为null
     */
    @Test
    void testUpdateCurrentUserWithNullRequest() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userController.updateCurrentUser(null);
        });
        assertEquals(ErrorCode.PARAMS_ERROR.getCode(), exception.getCode());
        assertEquals("参数错误", exception.getDescription());
    }

    /**
     * 测试更新当前用户信息 - 未登录
     */
    @Test
    void testUpdateCurrentUserWhenNotLoggedIn() {
        UserContext.clear(); // 清除用户上下文，模拟未登录状态

        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setUsername("新昵称");

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userController.updateCurrentUser(updateRequest);
        });
        assertEquals(ErrorCode.NOT_LOGIN.getCode(), exception.getCode());
    }

    /**
     * 测试更新当前用户信息 - 只更新用户名
     */
    @Test
    void testUpdateCurrentUserWithOnlyUsername() {
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setUsername("只更新用户名");

        Result<Boolean> result = userController.updateCurrentUser(updateRequest);
        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
    }

    /**
     * 测试更新当前用户信息 - 只更新头像
     */
    @Test
    void testUpdateCurrentUserWithOnlyAvatar() {
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setAvatarUrl("https://example.com/new-avatar.jpg");

        Result<Boolean> result = userController.updateCurrentUser(updateRequest);
        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
    }

    /**
     * 测试更新当前用户信息 - 更新所有字段
     */
    @Test
    void testUpdateCurrentUserWithAllFields() {
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setUsername("新昵称");
        updateRequest.setAvatarUrl("https://example.com/new-avatar.jpg");
        updateRequest.setGender("male");
        updateRequest.setPhone("13800138000");
        updateRequest.setEmail("newemail@example.com");

        Result<Boolean> result = userController.updateCurrentUser(updateRequest);
        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
    }

    /**
     * 测试更新当前用户信息 - 空请求（所有字段为null）
     */
    @Test
    void testUpdateCurrentUserWithEmptyRequest() {
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        // 所有字段都为null

        Result<Boolean> result = userController.updateCurrentUser(updateRequest);
        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
    }
}

