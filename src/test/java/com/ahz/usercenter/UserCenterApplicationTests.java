package com.ahz.usercenter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.security.NoSuchAlgorithmException;

/**
 * 启动类测试
 *
 * @author ahz
 * @version 1.0
 */
@SpringBootTest
class UserCenterApplicationTests {

    @Test
    @SuppressWarnings("null")
    void testDigest() throws NoSuchAlgorithmException {
        String password = "abcd" + "mypassword";
        byte[] bytes = password.getBytes();
        String newPassword = DigestUtils.md5DigestAsHex(bytes);
        System.out.println(newPassword);
    }

    @Test
    void contextLoads() {

    }

}