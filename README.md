# AHZ UserHub Backend - 用户中心后端服务

> 作者：ahz

一个基于 Spring Boot 的用户中心后端系统，实现了用户注册、登录、查询等核心功能。

## 项目简介

本项目是我在学习 Spring Boot 过程中开发的一个用户中心后端服务，采用 Spring Boot 框架实现完整的用户管理功能。作为学习项目，系统逻辑和功能并不复杂，代码量也不多，但正因如此，大家可以更轻松地学习到 **通用的技术和做项目的经验**。

本项目完全开源，欢迎同样在学习 Spring Boot 的同学参考、学习和交流！

## 技术栈

### 核心框架
- **Java 21** - 编程语言
- **Spring Boot 3.2.0** - 应用框架
- **Spring MVC** - Web 框架
- **Spring Security** - 安全框架（用于 BCrypt 密码加密）

### 数据访问
- **MyBatis 3.5.15** - ORM 框架
- **MyBatis Plus 3.5.5** - MyBatis 增强工具
- **MySQL 8.0** - 关系型数据库

### 缓存
- **Redis** - 分布式缓存（用于 Token 存储）

### 工具库
- **Lombok** - 简化 Java 代码
- **Apache Commons Lang3** - 工具类库
- **FastJSON2 2.0.43** - JSON 序列化

### 测试
- **JUnit 5** - 单元测试框架
- **Spring Boot Test** - 集成测试

## 核心功能

- ✅ **用户注册** - 支持账号密码注册，密码使用 BCrypt 加密
- ✅ **用户登录** - 基于 Token 的登录认证，Token 存储在 Redis
- ✅ **用户注销** - 清除 Redis 中的登录 Token
- ✅ **用户查询** - 管理员可查询用户列表（支持模糊搜索）
- ✅ **用户删除** - 管理员可删除用户（逻辑删除）
- ✅ **统一响应格式** - 统一的 API 响应结构
- ✅ **全局异常处理** - 统一的异常处理机制
- ✅ **用户信息脱敏** - 返回用户信息时自动脱敏
- ✅ **登录拦截器** - 基于 Token 的请求拦截和权限校验
- ✅ **跨域支持** - 配置 CORS 跨域访问

## 项目结构

```
ahz-userhub-backend/
├── src/
│   ├── main/
│   │   ├── java/com/ahz/usercenter/
│   │   │   ├── controller/          # 控制器层（RESTful API）
│   │   │   │   └── UserController.java
│   │   │   ├── service/             # 服务层（业务逻辑）
│   │   │   │   ├── UserService.java
│   │   │   │   └── impl/
│   │   │   │       └── UserServiceImpl.java
│   │   │   ├── mapper/              # 数据访问层（MyBatis）
│   │   │   │   └── UserMapper.java
│   │   │   ├── model/               # 数据模型
│   │   │   │   ├── domain/          # 实体类
│   │   │   │   │   ├── User.java
│   │   │   │   │   ├── request/     # 请求对象
│   │   │   │   │   │   ├── UserRegisterRequest.java
│   │   │   │   │   │   └── UserLoginRequest.java
│   │   │   │   │   └── response/    # 响应对象
│   │   │   │   │       └── LoginResponse.java
│   │   │   │   └── dto/             # 数据传输对象
│   │   │   │       └── UserDTO.java
│   │   │   ├── common/              # 公共组件
│   │   │   │   ├── Result.java
│   │   │   │   ├── ErrorCode.java
│   │   │   │   └── ResultUtils.java
│   │   │   ├── config/              # 配置类
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── WebMvcConfig.java
│   │   │   ├── interceptor/         # 拦截器
│   │   │   │   └── LoginInterceptor.java
│   │   │   ├── exception/           # 异常处理
│   │   │   │   ├── BusinessException.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── utils/               # 工具类
│   │   │   │   ├── TokenUtils.java
│   │   │   │   ├── UserContext.java
│   │   │   │   └── UserConvertor.java
│   │   │   ├── contant/             # 常量类
│   │   │   │   └── UserConstant.java
│   │   │   └── UserCenterApplication.java  # 启动类
│   │   └── resources/
│   │       ├── application.yml      # 配置文件
│   │       └── mapper/              # MyBatis XML 映射文件
│   │           └── UserMapper.xml
│   │       └── sql/                 # SQL 脚本
│   │           └── create_table.sql
│   └── test/                        # 测试代码
│       └── java/com/ahz/usercenter/
│           ├── UserCenterApplicationTests.java
│           └── service/
│               └── UserServiceTest.java
├── pom.xml                          # Maven 配置
└── README.md                        # 项目说明
```

## 快速开始

### 环境要求

- **JDK 21+**
- **Maven 3.6+**
- **MySQL 8.0+**
- **Redis 5.0+**（用于 Token 存储）

### 1. 数据库初始化

执行 `sql/create_table.sql` 文件中的 SQL 语句创建数据库和表：

```sql
-- 创建数据库
create database if not exists ahz_userhub;

-- 使用数据库
use ahz_userhub;

-- 创建用户表（SQL 文件中有完整表结构）
```

### 2. 配置文件修改

修改 `src/main/resources/application.yml` 中的配置信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ahz_userhub?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&useSSL=false
    username: root        # 修改为你的 MySQL 用户名
    password: 123456      # 修改为你的 MySQL 密码
  
  redis:
    host: localhost       # Redis 地址
    port: 6379           # Redis 端口
    database: 0           # Redis 数据库索引

server:
  port: 8080             # 服务端口
  servlet:
    context-path: /api   # 上下文路径
```

### 3. 启动 Redis

确保 Redis 服务已启动：

```bash
# Windows
redis-server

# Linux/Mac
redis-server
```

### 4. 运行项目

> **注意**：本项目使用标准 Maven 命令运行，请确保已安装 Maven 3.6+ 并配置好环境变量。可以通过 `mvn -v` 命令检查 Maven 是否安装成功。

#### 方式一：使用 Maven 插件运行（推荐）

```bash
mvn spring-boot:run
```

这种方式会直接启动 Spring Boot 应用，适合开发调试。

#### 方式二：打包后运行

```bash
# 打包项目（跳过测试可加快打包速度）
mvn clean package -DskipTests

# 运行 JAR 包
java -jar target/ahz-userhub-backend-0.0.1-SNAPSHOT.jar
```

打包后的 JAR 包可以部署到生产环境。

#### 方式三：使用 IDE 运行

1. 使用 IntelliJ IDEA 或 Eclipse 等 IDE 打开项目
2. 等待 Maven 依赖下载完成
3. 找到 `src/main/java/com/ahz/usercenter/UserCenterApplication.java`
4. 右键运行 `main` 方法

**提示**：首次运行前，请确保：
- MySQL 数据库已创建并执行了初始化 SQL
- Redis 服务已启动
- `application.yml` 中的配置信息已正确修改

### 5. 验证启动

启动成功后，控制台会显示类似以下信息：

```
Started UserCenterApplication in X.XXX seconds
```

此时可以访问 [Swagger UI](http://localhost:8080/api/swagger-ui/index.html) 查看和测试 API 接口。

如果启动失败，请检查：
1. 数据库连接配置是否正确
2. Redis 服务是否正常运行
3. 端口 8080 是否被占用

## API 接口文档

### Swagger 文档

项目已集成 Swagger（SpringDoc OpenAPI），启动项目后可通过以下地址访问 API 文档：

- **Swagger UI**: `http://localhost:8080/api/swagger-ui/index.html`
- **OpenAPI JSON**: `http://localhost:8080/api/v3/api-docs`

在 Swagger UI 中可以：
- 查看所有 API 接口
- 测试接口功能
- 查看请求参数和响应格式
- 查看接口说明和示例

📖 **详细使用指南请参考**: [SWAGGER_USAGE.md](./SWAGGER_USAGE.md)

### 基础信息

- **Base URL**: `http://localhost:8080/api`
- **Content-Type**: `application/json`
- **认证方式**: Token 认证（在请求头中携带 `Authorization: <token>`）

### 统一响应格式

```json
{
  "code": 0,           // 状态码，0 表示成功
  "data": {},          // 响应数据
  "message": "ok",     // 响应消息
  "description": ""    // 描述信息
}
```

### 接口列表

> 💡 **提示**: 建议使用 [Swagger UI](http://localhost:8080/api/swagger-ui/index.html) 查看和测试接口，所有接口都有详细的参数说明和示例。详细使用指南请参考 [SWAGGER_USAGE.md](./SWAGGER_USAGE.md)

| 接口 | 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|------|
| 用户注册 | POST | `/user/register` | 用户账号注册 | 否 |
| 用户登录 | POST | `/user/login` | 用户登录，返回 Token | 否 |
| 用户注销 | POST | `/user/logout` | 退出登录，清除 Token | 是 |
| 获取当前用户 | GET | `/user/getInfo` | 获取当前登录用户信息 | 是 |
| 更新用户信息 | POST | `/user/updateInfo` | 更新当前用户信息 | 是 |
| 搜索用户 | GET | `/admin/getUserList` | 管理员查询用户列表（支持模糊搜索） | 是（管理员） |
| 删除用户 | POST | `/admin/deleteUser` | 管理员删除用户（逻辑删除） | 是（管理员） |
| 更新用户（管理员） | POST | `/admin/updateUser` | 管理员更新用户信息 | 是（管理员） |

## 测试

运行单元测试：

```bash
mvn test
```

## 部署

### Docker 部署（推荐）

项目支持 Docker 容器化部署，提供一键启动完整开发环境（MySQL + Redis + 应用）：

```bash
# 启动所有服务
docker-compose up -d
```

**详细部署说明请参考**：[DOCKER.md](./DOCKER.md)

### 手动部署

#### 1. 启动 MySQL 和 Redis

确保 MySQL 和 Redis 服务已启动并正常运行。

#### 2. 构建和运行应用

```bash
# 打包
mvn clean package

# 运行
java -jar target/ahz-userhub-backend-0.0.1-SNAPSHOT.jar
```

## 常见问题

### 1. Redis 连接失败
- 确保 Redis 服务已启动
- 检查 `application.yml` 中的 Redis 配置

### 2. MySQL 连接失败
- 检查 MySQL 服务是否启动
- 验证数据库用户名和密码
- 确认数据库 `ahz_userhub` 已创建

### 3. Token 验证失败
- 检查请求头中是否携带 `Authorization` 字段
- 确认 Token 未过期（Redis 中是否存在）
- 验证 Token 格式是否正确

## 更新日志

### **v1.0** (2024-10-29)
- 实现用户注册、登录、查询、删除功能
- 实现统一响应格式（Result）
- 实现全局异常处理机制
- 完成基础项目架构搭建

### **v1.1** (2024-11-10)
- 引入 Spring Security BCrypt 密码加密
- 优化密码存储安全性，使用 BCrypt 哈希算法
- 更新密码验证逻辑，支持加密密码比对
- 优化用户注册和登录流程
- 集成 Swagger ，提供可视化 API 文档
- 在 Controller 中添加 Swagger 注解，完善接口文档说明

### **v2.0** (2024-11-18)
- 引入 Redis 缓存，用于 Token 存储
- 实现基于 Token 的登录认证机制
- 添加登录拦截器（LoginInterceptor），实现请求拦截和权限校验
- 实现用户上下文管理（UserContext），使用 ThreadLocal 存储当前用户
- 优化登录状态管理，Token 存储在 Redis 中
- 实现用户注销功能，清除 Redis 中的 Token
- 添加 LoginResponse 响应类，返回 Token 和用户信息
- 

### **v2.1** (2024-11-26)
- 配置 CORS 跨域支持，解决前后端分离部署问题
- 添加 UserConvertor 对象转换工具，实现 User 到 UserDTO 的转换
- 优化 WebMvcConfig 配置，完善跨域和拦截器配置
- 添加 SwaggerConfig 配置类，完善 API 文档配置
- 优化前后端分离部署支持
- 完善代码结构和工具类设计

### **v2.2** (2024-12-01)
- 添加 Dockerfile 支持，支持容器化部署
- 添加 docker-compose.yml，一键启动完整开发环境（MySQL + Redis + 应用）
- 添加 application-prod.yml 生产环境配置
- 优化部署文档，提供多种部署方式说明

## 版权声明

本项目仅供学习交流使用。
