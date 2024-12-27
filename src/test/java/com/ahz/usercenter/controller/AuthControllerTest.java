package com.ahz.usercenter.controller;

import com.ahz.usercenter.common.ErrorCode;
import com.ahz.usercenter.common.Result;
import com.ahz.usercenter.exception.BusinessException;
import com.ahz.usercenter.model.domain.request.UserLoginRequest;
import com.ahz.usercenter.model.domain.request.UserRegisterRequest;
import com.ahz.usercenter.model.domain.response.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * 认证控制器测试类
 * 测试用户注册、登录、注销等认证相关接口
 *
 * @author ahz
 * @version 3.0.1
 */
@SpringBootTest
class AuthControllerTest {

    @Resource
    private AuthController authController;

    /**
     * 测试用户登录 - 正常情况
     */
    @Test
    void testLogin() {
        UserLoginRequest request = new UserLoginRequest();
        request.setUserAccount("test@example.com");
        request.setUserPassword("12345678");

        Result<LoginResponse> result = authController.login(request, null);
        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
        assertNotNull(result.getData().getToken());
        assertNotNull(result.getData().getUser());
    }

    /**
     * 测试用户登录 - 请求为null
     */
    @Test
    void testLoginWithNullRequest() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authController.login(null, null);
        });
        assertEquals(ErrorCode.PARAMS_ERROR.getCode(), exception.getCode());
    }

    /**
     * 测试用户登录 - 账号为空
     */
    @Test
    void testLoginWithBlankAccount() {
        UserLoginRequest request = new UserLoginRequest();
        request.setUserAccount("");
        request.setUserPassword("12345678");

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authController.login(request, null);
        });
        assertEquals(ErrorCode.PARAMS_ERROR.getCode(), exception.getCode());
    }

    /**
     * 测试用户登录 - 密码为空
     */
    @Test
    void testLoginWithBlankPassword() {
        UserLoginRequest request = new UserLoginRequest();
        request.setUserAccount("test@example.com");
        request.setUserPassword("");

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authController.login(request, null);
        });
        assertEquals(ErrorCode.PARAMS_ERROR.getCode(), exception.getCode());
    }

    /**
     * 测试用户登录 - 账号和密码都为空
     */
    @Test
    void testLoginWithBlankAccountAndPassword() {
        UserLoginRequest request = new UserLoginRequest();
        request.setUserAccount("");
        request.setUserPassword("");

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authController.login(request, null);
        });
        assertEquals(ErrorCode.PARAMS_ERROR.getCode(), exception.getCode());
    }

    /**
     * 测试用户注册 - 正常情况
     */
    @Test
    void testRegister() {
        // 使用随机账号避免重复注册错误
        String randomAccount = "test" + System.currentTimeMillis() + "@example.com";
        
        UserRegisterRequest request = new UserRegisterRequest();
        request.setUserAccount(randomAccount);
        request.setUserPassword("12345678");
        request.setCheckPassword("12345678");

        Result<Long> result = authController.register(request);
        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
        assertTrue(result.getData() > 0);
    }

    /**
     * 测试用户注册 - 请求为null
     */
    @Test
    void testRegisterWithNullRequest() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authController.register(null);
        });
        assertEquals(ErrorCode.PARAMS_ERROR.getCode(), exception.getCode());
    }

    /**
     * 测试用户注册 - 账号为空
     */
    @Test
    void testRegisterWithBlankAccount() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setUserAccount("");
        request.setUserPassword("12345678");
        request.setCheckPassword("12345678");

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authController.register(request);
        });
        assertEquals(ErrorCode.PARAMS_ERROR.getCode(), exception.getCode());
        assertEquals("参数为空", exception.getDescription());
    }

    /**
     * 测试用户注册 - 密码为空
     */
    @Test
    void testRegisterWithBlankPassword() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setUserAccount("test@example.com");
        request.setUserPassword("");
        request.setCheckPassword("12345678");

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authController.register(request);
        });
        assertEquals(ErrorCode.PARAMS_ERROR.getCode(), exception.getCode());
        assertEquals("参数为空", exception.getDescription());
    }

    /**
     * 测试用户注册 - 确认密码为空
     */
    @Test
    void testRegisterWithBlankCheckPassword() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setUserAccount("test@example.com");
        request.setUserPassword("12345678");
        request.setCheckPassword("");

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authController.register(request);
        });
        assertEquals(ErrorCode.PARAMS_ERROR.getCode(), exception.getCode());
        assertEquals("参数为空", exception.getDescription());
    }

    /**
     * 测试用户注册 - 所有参数为空
     */
    @Test
    void testRegisterWithAllBlankParams() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setUserAccount("");
        request.setUserPassword("");
        request.setCheckPassword("");

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authController.register(request);
        });
        assertEquals(ErrorCode.PARAMS_ERROR.getCode(), exception.getCode());
        assertEquals("参数为空", exception.getDescription());
    }

    /**
     * 测试用户注销 - 正常情况（有Token）
     */
    @Test
    void testLogoutWithToken() {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getHeader("Authorization")).thenReturn("test-token-123");

        Result<Integer> result = authController.logout(mockRequest);
        assertEquals(0, result.getCode());
        assertEquals(1, result.getData());
    }

    /**
     * 测试用户注销 - 无Token
     */
    @Test
    void testLogoutWithoutToken() {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getHeader("Authorization")).thenReturn(null);

        Result<Integer> result = authController.logout(mockRequest);
        assertEquals(0, result.getCode());
        assertEquals(1, result.getData());
    }

    /**
     * 测试用户注销 - Token为空字符串
     */
    @Test
    void testLogoutWithEmptyToken() {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getHeader("Authorization")).thenReturn("");

        Result<Integer> result = authController.logout(mockRequest);
        assertEquals(0, result.getCode());
        assertEquals(1, result.getData());
    }
}

