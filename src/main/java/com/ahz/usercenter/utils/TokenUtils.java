package com.ahz.usercenter.utils;

import com.alibaba.fastjson2.JSON;
import com.ahz.usercenter.model.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Token 工具类
 * 基于 Redis 的 Token 管理
 *
 * @author ahz
 * @version 2.0
 */
@Component
@Slf4j
public class TokenUtils {

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;

    /**
     * Token 前缀
     */
    private static final String TOKEN_PREFIX = "USER_TOKEN:";

    /**
     * Token 过期时间（小时）
     */
    private static final int TOKEN_EXPIRE_HOURS = 2;

    /**
     * 生成 Token
     *
     * @param userId 用户 ID
     * @return Token 字符串
     */
    public String generateToken(Long userId) {
        return TOKEN_PREFIX + UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 存储 Token 和用户信息到 Redis
     *
     * @param token Token
     * @param user  用户信息（脱敏后的 UserDTO）
     */
    public void storeToken(@NonNull String token, @NonNull UserDTO user) {
        if (redisTemplate == null) {
            log.error("RedisTemplate is null, Redis may not be configured properly");
            throw new RuntimeException("Token 存储失败: Redis 未配置或连接失败，请检查 Redis 服务是否启动");
        }
        try {
            String userJson = JSON.toJSONString(user);
            if (userJson == null) {
                throw new RuntimeException("用户信息序列化失败");
            }
            redisTemplate.opsForValue().set(
                    token,
                    userJson,
                    TOKEN_EXPIRE_HOURS,
                    TimeUnit.HOURS
            );
            log.info("Token stored successfully: {}", token);
        } catch (Exception e) {
            log.error("Failed to store token: {}", e.getMessage(), e);
            String errorMsg = "Token 存储失败";
            if (e.getMessage() != null) {
                if (e.getMessage().contains("Connection refused") || e.getMessage().contains("无法连接")) {
                    errorMsg += ": Redis 连接失败，请检查 Redis 服务是否启动（localhost:6379）";
                } else if (e.getMessage().contains("timeout")) {
                    errorMsg += ": Redis 连接超时，请检查网络连接";
                } else {
                    errorMsg += ": " + e.getMessage();
                }
            }
            throw new RuntimeException(errorMsg, e);
        }
    }

    /**
     * 验证 Token 并自动续期
     *
     * @param token Token
     * @return 用户信息，如果 Token 无效或过期则返回 null
     */
    public UserDTO verifyToken(@NonNull String token) {
        try {
            String userJson = redisTemplate.opsForValue().get(token);
            if (userJson == null || userJson.isEmpty()) {
                log.info("Token not found or expired: {}", token);
                return null;
            }
            // 验证成功后自动续期（刷新过期时间为2小时）
            redisTemplate.expire(token, TOKEN_EXPIRE_HOURS, TimeUnit.HOURS);
            return JSON.parseObject(userJson, UserDTO.class);
        } catch (Exception e) {
            log.error("Failed to verify token", e);
            return null;
        }
    }

    /**
     * 删除 Token（用于退出登录）
     *
     * @param token Token
     */
    public void deleteToken(@NonNull String token) {
        try {
            redisTemplate.delete(token);
            log.info("Token deleted successfully: {}", token);
        } catch (Exception e) {
            log.error("Failed to delete token", e);
        }
    }
}

