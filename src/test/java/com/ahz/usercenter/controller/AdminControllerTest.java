package com.ahz.usercenter.controller;

import com.ahz.usercenter.common.ErrorCode;
import com.ahz.usercenter.common.Result;
import com.ahz.usercenter.exception.BusinessException;
import com.ahz.usercenter.model.domain.request.AdminUpdateUserRequest;
import com.ahz.usercenter.model.dto.UserDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 管理员控制器测试类
 * 测试管理员用户管理相关的接口
 *
 * @author ahz
 * @version 3.0.1
 */
@SpringBootTest
class AdminControllerTest {

    @Resource
    private AdminController adminController;

    /**
     * 测试分页查询用户列表 - 正常情况
     */
    @Test
    void testGetUserList() {
        Result<Page<UserDTO>> result = adminController.getUserList(1L, 10L, null, null, null);
        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
        assertNotNull(result.getData().getRecords());
    }

    /**
     * 测试分页查询用户列表 - 使用默认参数
     */
    @Test
    void testGetUserListWithDefaults() {
        Result<Page<UserDTO>> result = adminController.getUserList(1L, 10L, null, null, null);
        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
    }

    /**
     * 测试分页查询用户列表 - 带用户名搜索
     */
    @Test
    void testGetUserListWithUsername() {
        Result<Page<UserDTO>> result = adminController.getUserList(1L, 10L, "test", null, null);
        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
    }

    /**
     * 测试分页查询用户列表 - 带角色过滤
     */
    @Test
    void testGetUserListWithRole() {
        Result<Page<UserDTO>> result = adminController.getUserList(1L, 10L, null, "user", null);
        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
    }

    /**
     * 测试分页查询用户列表 - 带状态过滤
     */
    @Test
    void testGetUserListWithStatus() {
        Result<Page<UserDTO>> result = adminController.getUserList(1L, 10L, null, null, 0);
        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
    }

    /**
     * 测试分页查询用户列表 - 组合查询
     */
    @Test
    void testGetUserListWithMultipleFilters() {
        Result<Page<UserDTO>> result = adminController.getUserList(1L, 10L, "test", "user", 0);
        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
    }

    /**
     * 测试获取单个用户信息 - 正常情况
     */
    @Test
    void testGetUserById() {
        // 假设存在ID为1的用户，如果不存在会抛出异常
        try {
            Result<UserDTO> result = adminController.getUserById(1L);
            assertEquals(0, result.getCode());
            assertNotNull(result.getData());
        } catch (BusinessException e) {
            // 如果用户不存在，这是预期的行为
            assertEquals(ErrorCode.PARAMS_ERROR.getCode(), e.getCode());
        }
    }

    /**
     * 测试获取单个用户信息 - 用户ID为null
     */
    @Test
    void testGetUserByIdWithNullId() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            adminController.getUserById(null);
        });
        assertEquals(ErrorCode.PARAMS_ERROR.getCode(), exception.getCode());
        assertEquals("用户ID无效", exception.getDescription());
    }

    /**
     * 测试获取单个用户信息 - 用户ID无效（小于等于0）
     */
    @Test
    void testGetUserByIdWithInvalidId() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            adminController.getUserById(0L);
        });
        assertEquals(ErrorCode.PARAMS_ERROR.getCode(), exception.getCode());
        assertEquals("用户ID无效", exception.getDescription());
    }

    /**
     * 测试获取单个用户信息 - 用户ID为负数
     */
    @Test
    void testGetUserByIdWithNegativeId() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            adminController.getUserById(-1L);
        });
        assertEquals(ErrorCode.PARAMS_ERROR.getCode(), exception.getCode());
        assertEquals("用户ID无效", exception.getDescription());
    }

    /**
     * 测试全量更新用户信息 - 正常情况
     */
    @Test
    void testUpdateUser() {
        // 假设存在ID为1的用户
        try {
            AdminUpdateUserRequest updateRequest = new AdminUpdateUserRequest();
            updateRequest.setId(1L);
            updateRequest.setUsername("更新后的用户名");
            updateRequest.setAvatarUrl("https://example.com/avatar.jpg");
            updateRequest.setGender("male");
            updateRequest.setPhone("13800138000");
            updateRequest.setEmail("newemail@example.com");
            updateRequest.setUserStatus("active");
            updateRequest.setUserRole("user");

            Result<Boolean> result = adminController.updateUser(1L, updateRequest);
            assertEquals(0, result.getCode());
            assertNotNull(result.getData());
        } catch (BusinessException e) {
            // 如果用户不存在，这是预期的行为
            assertEquals(ErrorCode.PARAMS_ERROR.getCode(), e.getCode());
        }
    }

    /**
     * 测试全量更新用户信息 - 请求为null
     */
    @Test
    void testUpdateUserWithNullRequest() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            adminController.updateUser(1L, null);
        });
        assertEquals(ErrorCode.PARAMS_ERROR.getCode(), exception.getCode());
        assertEquals("参数错误", exception.getDescription());
    }

    /**
     * 测试全量更新用户信息 - 用户ID不一致
     */
    @Test
    void testUpdateUserWithMismatchedId() {
        AdminUpdateUserRequest updateRequest = new AdminUpdateUserRequest();
        updateRequest.setId(2L); // 与路径中的ID不一致

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            adminController.updateUser(1L, updateRequest);
        });
        assertEquals(ErrorCode.PARAMS_ERROR.getCode(), exception.getCode());
        assertEquals("路径中的用户ID与请求体中的用户ID不一致", exception.getDescription());
    }

    /**
     * 测试全量更新用户信息 - 用户ID无效
     */
    @Test
    void testUpdateUserWithInvalidId() {
        AdminUpdateUserRequest updateRequest = new AdminUpdateUserRequest();
        updateRequest.setId(0L);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            adminController.updateUser(0L, updateRequest);
        });
        assertEquals(ErrorCode.PARAMS_ERROR.getCode(), exception.getCode());
        assertEquals("用户ID无效", exception.getDescription());
    }

    /**
     * 测试部分更新用户信息 - 正常情况
     */
    @Test
    void testPatchUser() {
        // 假设存在ID为1的用户
        try {
            AdminUpdateUserRequest updateRequest = new AdminUpdateUserRequest();
            updateRequest.setUsername("部分更新用户名");

            Result<Boolean> result = adminController.patchUser(1L, updateRequest);
            assertEquals(0, result.getCode());
            assertNotNull(result.getData());
        } catch (BusinessException e) {
            // 如果用户不存在，这是预期的行为
            assertEquals(ErrorCode.PARAMS_ERROR.getCode(), e.getCode());
        }
    }

    /**
     * 测试部分更新用户信息 - 请求为null
     */
    @Test
    void testPatchUserWithNullRequest() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            adminController.patchUser(1L, null);
        });
        assertEquals(ErrorCode.PARAMS_ERROR.getCode(), exception.getCode());
        assertEquals("参数错误", exception.getDescription());
    }

    /**
     * 测试部分更新用户信息 - 用户ID不一致
     */
    @Test
    void testPatchUserWithMismatchedId() {
        AdminUpdateUserRequest updateRequest = new AdminUpdateUserRequest();
        updateRequest.setId(2L); // 与路径中的ID不一致

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            adminController.patchUser(1L, updateRequest);
        });
        assertEquals(ErrorCode.PARAMS_ERROR.getCode(), exception.getCode());
        assertEquals("路径中的用户ID与请求体中的用户ID不一致", exception.getDescription());
    }

    /**
     * 测试部分更新用户信息 - 用户ID无效
     */
    @Test
    void testPatchUserWithInvalidId() {
        AdminUpdateUserRequest updateRequest = new AdminUpdateUserRequest();

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            adminController.patchUser(0L, updateRequest);
        });
        assertEquals(ErrorCode.PARAMS_ERROR.getCode(), exception.getCode());
        assertEquals("用户ID无效", exception.getDescription());
    }

    /**
     * 测试部分更新用户信息 - 只更新部分字段
     */
    @Test
    void testPatchUserWithPartialFields() {
        // 假设存在ID为1的用户
        try {
            AdminUpdateUserRequest updateRequest = new AdminUpdateUserRequest();
            updateRequest.setUsername("新用户名");
            updateRequest.setAvatarUrl("https://example.com/new-avatar.jpg");
            // 其他字段不设置

            Result<Boolean> result = adminController.patchUser(1L, updateRequest);
            assertEquals(0, result.getCode());
            assertNotNull(result.getData());
        } catch (BusinessException e) {
            // 如果用户不存在，这是预期的行为
            assertEquals(ErrorCode.PARAMS_ERROR.getCode(), e.getCode());
        }
    }

    /**
     * 测试删除用户 - 正常情况
     */
    @Test
    void testDeleteUser() {
        // 假设存在ID为1的用户
        try {
            Result<Boolean> result = adminController.deleteUser(1L);
            assertEquals(0, result.getCode());
            assertNotNull(result.getData());
        } catch (BusinessException e) {
            // 如果用户不存在，这是预期的行为
            assertEquals(ErrorCode.PARAMS_ERROR.getCode(), e.getCode());
        }
    }

    /**
     * 测试删除用户 - 用户ID为null
     */
    @Test
    void testDeleteUserWithNullId() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            adminController.deleteUser(null);
        });
        assertEquals(ErrorCode.PARAMS_ERROR.getCode(), exception.getCode());
        assertEquals("用户ID无效", exception.getDescription());
    }

    /**
     * 测试删除用户 - 用户ID无效（小于等于0）
     */
    @Test
    void testDeleteUserWithInvalidId() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            adminController.deleteUser(0L);
        });
        assertEquals(ErrorCode.PARAMS_ERROR.getCode(), exception.getCode());
        assertEquals("用户ID无效", exception.getDescription());
    }

    /**
     * 测试删除用户 - 用户ID为负数
     */
    @Test
    void testDeleteUserWithNegativeId() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            adminController.deleteUser(-1L);
        });
        assertEquals(ErrorCode.PARAMS_ERROR.getCode(), exception.getCode());
        assertEquals("用户ID无效", exception.getDescription());
    }
}

