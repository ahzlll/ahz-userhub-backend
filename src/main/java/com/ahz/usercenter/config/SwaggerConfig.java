package com.ahz.usercenter.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger 配置类
 * 配置 API 文档信息
 *
 * @author ahz
 * @version 2.2
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AHZ UserHub API 文档")
                        .version("2.2")
                        .description("AHZ UserHub 用户中心后端服务 API 接口文档\n\n" +
                                "**使用说明：**\n" +
                                "1. 点击右上角的 🔒 **Authorize** 按钮\n" +
                                "2. 在弹出框中输入你的 Token（登录后获取）\n" +
                                "3. 点击 **Authorize** 锁定 Token，之后所有请求都会自动携带\n" +
                                "4. 测试接口时，请求体已预设示例值，可直接使用或修改")
                        .contact(new Contact()
                                .name("ahz")
                                .email("ahz@example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(getServers())
                // 添加安全方案（Bearer Token）
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()));
    }

    /**
     * 配置服务器地址
     */
    private List<Server> getServers() {
        List<Server> servers = new ArrayList<>();
        servers.add(new Server().url("http://localhost:8080/api").description("本地开发环境"));
        return servers;
    }

    /**
     * 创建 Bearer Token 安全方案
     * 注意：这里使用 header 方式，因为拦截器从 Authorization header 读取 token
     */
    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("Authorization")
                .description("请输入 Token，格式：直接输入 token 值即可（不需要 Bearer 前缀）\n\n" +
                        "获取 Token：\n" +
                        "1. 先调用登录接口 `/user/login`\n" +
                        "2. 从响应中复制 `data.token` 的值\n" +
                        "3. 粘贴到此处并点击 Authorize");
    }
}

