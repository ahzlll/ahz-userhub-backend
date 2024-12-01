# Swagger 使用指南

1. **一键锁定 Token** - 点击右上角 🔒 Authorize 按钮，输入 token 后自动应用到所有请求
2. **预设请求体示例** - 所有接口的请求体都已预设示例值，可直接使用或修改

## 📖 使用步骤

### 1. 获取 Token

首先调用登录接口获取 Token：

```
POST /user/login
```

请求体示例（已预设）：
```json
{
  "userAccount": "testuser",
  "userPassword": "12345678"
}
```

从响应中复制 `data.token` 的值。

### 2. 锁定 Token

1. 在 Swagger UI 页面右上角，点击 🔒 **Authorize** 按钮
2. 在弹出的对话框中，将复制的 token 粘贴到输入框
3. 点击 **Authorize** 按钮锁定
4. 之后所有需要认证的接口都会自动携带这个 token

### 3. 测试接口

现在可以直接测试任何接口：

- **请求体已预设示例值** - 可以直接点击 "Try it out" 使用，或根据需要修改
- **Token 自动携带** - 无需手动添加到每个请求中

## 🔑 需要认证的接口

以下接口需要先锁定 Token：

- `/user/logout` - 退出登录
- `/user/getInfo` - 获取当前用户信息
- `/user/updateInfo` - 更新用户信息
- `/admin/*` - 所有管理员接口

## 📝 请求体示例

所有请求体都已预设示例值：

- **登录**: `userAccount: "testuser"`, `userPassword: "12345678"`
- **注册**: `userAccount: "testuser"`, `userPassword: "12345678"`, `checkPassword: "12345678"`
- **更新用户**: 包含用户名、头像、性别、电话、邮箱等示例值
- **管理员更新**: 包含所有字段的示例值

## 💡 提示

- Token 会保存在浏览器中，刷新页面后仍然有效
- 如果需要更换 Token，再次点击 Authorize 按钮即可
- 所有示例值都可以根据实际情况修改

