package com.ahz.usercenter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 配置类
 * 用于注册 BCryptPasswordEncoder Bean 和配置 Web 安全规则
 *
 * @author ahz
 * @version 2.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * 注册 BCryptPasswordEncoder Bean
     * BCrypt 是安全的密码哈希算法，每次加密结果不同但验证时能正确匹配
     *
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置 Web 安全规则
     * 允许所有请求通过，由自定义拦截器（LoginInterceptor）处理 Token 认证
     *
     * @param http HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception 配置异常
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 禁用 CSRF（因为使用 Token 认证）
                .csrf(AbstractHttpConfigurer::disable)
                // 配置会话管理为无状态（使用 Token，不需要 Session）
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 允许所有请求通过，认证由拦截器处理
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                // 禁用默认的登录页面
                .formLogin(AbstractHttpConfigurer::disable)
                // 禁用 HTTP Basic 认证
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }
}

