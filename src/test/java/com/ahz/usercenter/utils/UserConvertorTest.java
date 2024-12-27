package com.ahz.usercenter.utils;

import com.ahz.usercenter.model.domain.User;
import com.ahz.usercenter.model.dto.UserDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户转换工具类测试
 *
 * @author ahz
 * @version 3.0.1
 */
class UserConvertorTest {

    /**
     * 测试将User转换为UserDTO - 正常情况
     */
    @Test
    void testToDTO() {
        User user = new User();
        user.setId(1L);
        user.setUsername("测试用户");
        user.setUserAccount("test@example.com");
        user.setAvatarUrl("https://example.com/avatar.jpg");
        user.setGender("male");
        user.setPhone("12345678901");
        user.setEmail("test@example.com");
        user.setUserStatus("active");
        user.setUserRole("user");
        user.setUserPassword("encrypted_password");

        UserDTO userDTO = UserConvertor.toDTO(user);

        assertNotNull(userDTO);
        assertEquals(user.getId(), userDTO.getId());
        assertEquals(user.getUsername(), userDTO.getUsername());
        assertEquals(user.getUserAccount(), userDTO.getUserAccount());
        assertEquals(user.getAvatarUrl(), userDTO.getAvatarUrl());
        assertEquals(user.getGender(), userDTO.getGender());
        assertEquals(user.getPhone(), userDTO.getPhone());
        assertEquals(user.getEmail(), userDTO.getEmail());
        assertEquals(user.getUserStatus(), userDTO.getUserStatus());
        assertEquals(user.getUserRole(), userDTO.getUserRole());
    }

    /**
     * 测试将User转换为UserDTO - User为null
     */
    @Test
    void testToDTOWithNullUser() {
        UserDTO userDTO = UserConvertor.toDTO(null);
        assertNull(userDTO);
    }

    /**
     * 测试将User转换为UserDTO - 部分字段为null
     */
    @Test
    void testToDTOWithPartialNullFields() {
        User user = new User();
        user.setId(1L);
        user.setUsername("测试用户");
        // 其他字段为null

        UserDTO userDTO = UserConvertor.toDTO(user);

        assertNotNull(userDTO);
        assertEquals(user.getId(), userDTO.getId());
        assertEquals(user.getUsername(), userDTO.getUsername());
        assertNull(userDTO.getUserAccount());
        assertNull(userDTO.getAvatarUrl());
    }

    /**
     * 测试将User转换为UserDTO - 空User对象
     */
    @Test
    void testToDTOWithEmptyUser() {
        User user = new User();
        // 所有字段都为默认值

        UserDTO userDTO = UserConvertor.toDTO(user);

        assertNotNull(userDTO);
        assertNull(userDTO.getId());
        assertNull(userDTO.getUsername());
    }
}

