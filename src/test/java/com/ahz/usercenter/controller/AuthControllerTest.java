package com.ahz.usercenter.controller;

import com.ahz.usercenter.common.Result;
import com.ahz.usercenter.model.domain.request.UserLoginRequest;
import com.ahz.usercenter.model.domain.request.UserRegisterRequest;
import com.ahz.usercenter.model.domain.response.LoginResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 认证控制器测试类
 * 测试用户注册、登录、注销等认证相关接口
 *
 * @author ahz
 * @version 3.0
 */
@SpringBootTest
class AuthControllerTest {

    @Resource
    private AuthController authController;

    /**
     * 测试用户登录
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
     * 测试用户注册
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
}

