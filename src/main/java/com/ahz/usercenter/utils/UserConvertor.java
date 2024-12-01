package com.ahz.usercenter.utils;

import com.ahz.usercenter.model.domain.User;
import com.ahz.usercenter.model.dto.UserDTO;
import org.springframework.beans.BeanUtils;

/**
 * 用户对象转换工具类
 * 将 User 转换为 UserDTO（脱敏）
 *
 * @author ahz
 * @version 2.1
 */
public class UserConvertor {

    /**
     * 将 User 转换为 UserDTO
     *
     * @param user 用户实体
     * @return 用户 DTO
     */
    public static UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }
}

