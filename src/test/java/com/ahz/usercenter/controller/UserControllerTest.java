package com.ahz.usercenter.controller;

import com.ahz.usercenter.common.Result;
import com.ahz.usercenter.model.domain.request.UserLoginRequest;
import com.ahz.usercenter.model.domain.response.LoginResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户控制器测试类
 *
 * @author ahz
 * @version 1.0
 */
@SpringBootTest
class UserControllerTest {

    @Resource
    private UserController userController;

    @Test
    void testLogin() {
        UserLoginRequest request = new UserLoginRequest();
        request.setUserAccount("test@example.com");
        request.setUserPassword("12345678");

        Result<LoginResponse> result = userController.login(request, null);
        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
        assertNotNull(result.getData().getToken());
    }
}

