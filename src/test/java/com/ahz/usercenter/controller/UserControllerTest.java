package com.ahz.usercenter.controller;

import com.ahz.usercenter.common.Result;
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
 * @version 3.0
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
     * 测试获取当前用户信息
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
     * 测试更新当前用户信息
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
}

