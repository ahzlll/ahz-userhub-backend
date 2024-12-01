# Docker 部署指南

本项目支持 Docker 容器化部署，提供了两种方式：

## 🚀 快速开始

### 方式一：Docker Compose（推荐）

一键启动完整开发环境（MySQL + Redis + 应用）：

```bash
# 启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps

# 查看应用日志
docker-compose logs -f backend

# 查看所有服务日志
docker-compose logs -f

# 停止所有服务
docker-compose down

# 停止并删除数据卷（清理所有数据）
docker-compose down -v
```

**访问地址**：
- 应用：`http://localhost:8080/api`
- Swagger UI：`http://localhost:8080/api/swagger-ui/index.html`
- MySQL：`localhost:3306`
- Redis：`localhost:6379`

### 方式二：单独使用 Dockerfile

如果只需要构建应用镜像：

```bash
# 构建镜像
docker build -t ahz-userhub-backend:latest .

# 运行容器（需要先启动 MySQL 和 Redis）
docker run -d \
  --name ahz-userhub-backend \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/ahz_userhub?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&useSSL=false \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=123456 \
  -e SPRING_REDIS_HOST=host.docker.internal \
  -e SPRING_REDIS_PORT=6379 \
  ahz-userhub-backend:latest
```

## 📋 服务说明

### MySQL
- **镜像**：`mysql:8.0`
- **端口**：`3306`
- **数据库**：`ahz_userhub`
- **用户名**：`root`
- **密码**：`123456`（可在 docker-compose.yml 中修改）
- **数据持久化**：`mysql-data` 卷
- **自动初始化**：会自动执行 `src/main/resources/sql/create_table.sql`

### Redis
- **镜像**：`redis:7-alpine`
- **端口**：`6379`
- **数据持久化**：`redis-data` 卷（AOF 模式）
- **用途**：Token 存储

### Backend（应用）
- **基础镜像**：`eclipse-temurin:21-jre-alpine`
- **端口**：`8080`
- **构建方式**：多阶段构建（Maven 构建 + JRE 运行）
- **依赖**：等待 MySQL 和 Redis 健康检查通过后启动

## 🔧 配置说明

### 环境变量

应用支持通过环境变量覆盖配置：

- `SPRING_PROFILES_ACTIVE`：Spring 配置文件（默认：prod）
- `SPRING_DATASOURCE_URL`：数据库连接 URL
- `SPRING_DATASOURCE_USERNAME`：数据库用户名
- `SPRING_DATASOURCE_PASSWORD`：数据库密码
- `SPRING_REDIS_HOST`：Redis 主机地址
- `SPRING_REDIS_PORT`：Redis 端口
- `SERVER_PORT`：应用端口
- `SERVER_SERVLET_CONTEXT_PATH`：上下文路径

### 修改配置

#### 修改数据库密码

编辑 `docker-compose.yml`：

```yaml
mysql:
  environment:
    MYSQL_ROOT_PASSWORD: your_password  # 修改这里
```

同时修改 `backend` 服务的环境变量：

```yaml
backend:
  environment:
    SPRING_DATASOURCE_PASSWORD: your_password  # 修改这里
```

#### 修改端口

编辑 `docker-compose.yml` 中的 `ports` 配置：

```yaml
backend:
  ports:
    - "8081:8080"  # 主机端口:容器端口
```

## 🐛 常见问题

### 1. 端口被占用

如果 3306、6379 或 8080 端口被占用，可以修改 `docker-compose.yml` 中的端口映射：

```yaml
mysql:
  ports:
    - "3307:3306"  # 改为 3307

redis:
  ports:
    - "6380:6379"  # 改为 6380

backend:
  ports:
    - "8081:8080"  # 改为 8081
```

### 2. 应用启动失败

查看应用日志：

```bash
docker-compose logs backend
```

常见原因：
- MySQL 或 Redis 未启动
- 数据库连接配置错误
- 端口冲突

### 3. 数据持久化

所有数据都保存在 Docker 卷中：
- MySQL 数据：`mysql-data` 卷
- Redis 数据：`redis-data` 卷

即使删除容器，数据也不会丢失。要清理数据：

```bash
docker-compose down -v
```

### 4. 重新构建镜像

如果修改了代码，需要重新构建：

```bash
docker-compose build backend
docker-compose up -d
```

或者强制重新构建：

```bash
docker-compose build --no-cache backend
docker-compose up -d
```

## 📝 开发建议

### 开发环境

建议在开发时：
1. 使用本地 MySQL 和 Redis（不通过 Docker）
2. 使用 IDE 直接运行应用
3. 使用 `application.yml` 配置

### 生产环境

生产环境建议：
1. 使用 Docker Compose 或 Kubernetes
2. 修改默认密码
3. 配置 SSL/TLS
4. 设置资源限制
5. 配置日志收集

## 🔗 相关文档

- [Swagger 使用指南](./SWAGGER_USAGE.md)
- [README](./README.md)

