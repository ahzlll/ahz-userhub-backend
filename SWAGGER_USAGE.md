# Swagger 使用指南

## 🌐 访问地址

启动项目后，在浏览器中访问：

- **Swagger UI**: `http://localhost:8080/api/swagger-ui/index.html`
- **OpenAPI JSON**: `http://localhost:8080/api/v3/api-docs`

## ✨ 主要特性

1. **一键锁定 Token** - 点击右上角 🔒 Authorize 按钮，输入 token 后自动应用到所有请求
2. **预设请求体示例** - 所有接口的请求体都已预设示例值，可直接使用或修改
3. **在线测试** - 直接在浏览器中测试所有 API 接口，无需额外工具

## 📖 使用步骤

### 1. 获取 Token

首先调用登录接口获取 Token：

1. 在 Swagger UI 中找到 `POST /auth/login` 接口
2. 点击接口展开详情
3. 点击 **"Try it out"** 按钮
4. 请求体已预设示例值，可以直接使用或修改：
   ```json
   {
     "userAccount": "testuser",
     "userPassword": "12345678"
   }
   ```
5. 点击 **"Execute"** 按钮执行请求
6. 在响应结果中找到 `data.token` 字段，复制其值

**响应示例**：
```json
{
  "code": 0,
  "data": {
    "token": "your-token-value-here",
    "user": { ... }
  },
  "message": "ok"
}
```

> 💡 **提示**：如果没有测试账号，可以先调用 `/auth/register` 接口注册一个新账号。

### 2. 锁定 Token

1. 在 Swagger UI 页面右上角，点击 🔒 **Authorize** 按钮
2. 在弹出的对话框中，将复制的 token 值**直接粘贴**到输入框
   > ⚠️ **注意**：直接输入 token 值即可，**不需要**添加 "Bearer " 前缀
3. 点击 **Authorize** 按钮锁定
4. 对话框会显示 "Authorized"，表示 Token 已成功锁定
5. 之后所有需要认证的接口都会自动携带这个 token

### 3. 测试接口

现在可以直接测试任何接口：

1. 找到要测试的接口（如 `GET /api/v1/users/me`）
2. 点击接口展开详情
3. 点击 **"Try it out"** 按钮
4. 修改请求参数（如果需要）
5. 点击 **"Execute"** 按钮执行请求
6. 在下方查看响应结果，包括：
   - **Response Code**：HTTP 状态码
   - **Response Body**：响应体（JSON 格式）
   - **Response Headers**：响应头信息

**特性说明**：
- **请求体已预设示例值** - 可以直接使用，或根据需要修改
- **Token 自动携带** - 无需手动添加到每个请求中
- **实时查看响应** - 可以直接在页面上查看接口返回的数据

## 🔑 需要认证的接口

以下接口需要先锁定 Token：

### 普通用户接口（需要登录）

- `POST /auth/logout` - 退出登录
- `GET /api/v1/users/me` - 获取当前用户信息
- `PATCH /api/v1/users/me` - 更新用户信息（部分更新）

### 管理员接口（需要登录 + admin 角色）

- `GET /api/v1/users` - 查询用户列表（支持分页、搜索和过滤）
- `GET /api/v1/users/{userId}` - 获取指定用户信息
- `PUT /api/v1/users/{userId}` - 全量更新用户信息
- `PATCH /api/v1/users/{userId}` - 部分更新用户信息
- `DELETE /api/v1/users/{userId}` - 删除用户（逻辑删除）

> ⚠️ **注意**：管理员接口需要用户角色为 `admin`，普通用户无法访问。如果提示"无权限"，请使用管理员账号登录。

## 📝 请求体示例

所有请求体都已预设示例值：

- **登录**: `userAccount: "testuser"`, `userPassword: "12345678"`
- **注册**: `userAccount: "testuser"`, `userPassword: "12345678"`, `checkPassword: "12345678"`
- **更新用户**: 包含用户名、头像、性别、电话、邮箱等示例值
- **管理员更新**: 包含所有字段的示例值

## 💡 提示

- **Token 持久化**：Token 会保存在浏览器中，刷新页面后仍然有效
- **更换 Token**：如果需要更换 Token，再次点击 Authorize 按钮，输入新的 token 即可
- **修改示例值**：所有示例值都可以根据实际情况修改
- **查看接口详情**：每个接口都有详细的参数说明、示例值和响应格式说明

## ❓ 常见问题

### 1. Token 过期或无效

**问题**：调用接口时返回 401 错误，提示"未登录"或"登录已过期"

**解决方法**：
- 重新调用 `/auth/login` 接口获取新的 Token
- 在 Authorize 对话框中更新 Token

### 2. 无权限访问管理员接口

**问题**：调用管理员接口时返回 403 错误，提示"无权限"

**解决方法**：
- 确保使用的账号角色为 `admin`
- 使用管理员账号重新登录获取 Token

### 3. 接口调用失败

**问题**：点击 Execute 后没有响应或报错

**解决方法**：
- 检查服务是否正常启动（查看控制台日志）
- 检查数据库和 Redis 是否正常运行
- 检查请求参数格式是否正确（JSON 格式）
- 查看响应中的错误信息，根据提示修改请求参数

### 4. 找不到 Token 字段

**问题**：登录后响应中没有 `data.token` 字段

**解决方法**：
- 检查登录是否成功（`code` 是否为 0）
- 查看 `data` 对象的结构，Token 可能在 `data.token` 或 `data.loginResponse.token` 中
- 查看完整的响应体，确认 Token 的位置

