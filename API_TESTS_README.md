# SummerPlan Manager API 测试脚本

本目录包含了用于测试 SummerPlan Manager 后端 API 的 Shell 脚本。

## 测试脚本列表

### 1. 单个接口测试脚本

- `test_login.sh` - 测试用户登录接口 (`POST /api/auth/login`)
- `test_register.sh` - 测试用户注册接口 (`POST /api/auth/register`)
- `test_me.sh` - 测试获取当前用户信息接口 (`GET /api/auth/me`)
- `test_profile.sh` - 测试获取用户详细信息接口 (`GET /api/auth/profile`)
- `test_logout.sh` - 测试用户登出接口 (`POST /api/auth/logout`)

### 2. 综合测试脚本

- `test_all_apis.sh` - 综合测试所有API接口，包含完整的测试流程和结果统计

## 使用方法

### 前置条件

1. 确保后端服务正在运行在 `http://localhost:8080`
2. 确保数据库中有默认用户 `admin` (密码: `admin123`)

### 运行单个测试

```bash
# 给脚本添加执行权限
chmod +x test_*.sh

# 运行登录测试
./test_login.sh

# 运行注册测试
./test_register.sh

# 运行获取用户信息测试（需要先登录）
./test_me.sh

# 运行获取用户详细信息测试（需要先登录）
./test_profile.sh

# 运行登出测试
./test_logout.sh
```

### 运行综合测试

```bash
# 运行所有API的综合测试
./test_all_apis.sh
```

## API 接口说明

### 认证相关接口

| 接口 | 方法 | 路径 | 描述 | 是否需要认证 |
|------|------|------|------|-------------|
| 用户注册 | POST | `/api/auth/register` | 注册新用户 | 否 |
| 用户登录 | POST | `/api/auth/login` | 用户登录获取token | 否 |
| 获取当前用户信息 | GET | `/api/auth/me` | 获取当前登录用户基本信息 | 是 |
| 获取用户详细信息 | GET | `/api/auth/profile` | 获取当前登录用户详细信息 | 是 |
| 用户登出 | POST | `/api/auth/logout` | 用户登出 | 否 |

### 请求/响应格式

#### 登录请求
```json
{
  "username": "admin",
  "password": "admin123"
}
```

#### 登录响应
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "user": {
      "id": 1,
      "username": "admin",
      "email": "admin@example.com",
      "realName": "管理员",
      "phone": "13800138000",
      "avatar": null,
      "role": "ADMIN"
    }
  }
}
```

#### 注册请求
```json
{
  "username": "testuser",
  "password": "test123"
}
```

## 测试覆盖场景

### 正常流程测试
- ✅ 用户注册
- ✅ 用户登录
- ✅ 获取用户信息（需要认证）
- ✅ 用户登出

### 异常场景测试
- ✅ 重复注册（应该失败）
- ✅ 使用错误密码登录（应该失败）
- ✅ 使用无效token访问受保护接口（应该失败）

### 认证测试
- ✅ 无token访问受保护接口
- ✅ 使用有效token访问受保护接口
- ✅ 使用无效token访问受保护接口

## 故障排除

### 常见问题

1. **连接被拒绝**
   - 确保后端服务正在运行
   - 检查端口8080是否被占用

2. **认证失败**
   - 确保数据库中有默认用户 `admin`
   - 检查JWT配置是否正确

3. **数据库连接失败**
   - 检查数据库配置
   - 确保数据库服务正在运行

### 调试技巧

1. 查看详细的HTTP响应：
   ```bash
   curl -v -X POST http://localhost:8080/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username": "admin", "password": "admin123"}'
   ```

2. 检查后端日志：
   ```bash
   tail -f logs/application.log
   ```

## 扩展测试

如果需要添加新的API接口测试，可以参考现有脚本的格式：

1. 创建新的测试脚本文件
2. 添加适当的请求头和认证信息
3. 验证响应状态码和内容
4. 更新综合测试脚本

## 注意事项

- 所有测试脚本都使用 `admin/admin123` 作为默认测试用户
- 测试脚本会创建临时用户，可能影响数据库状态
- 建议在测试环境中运行，避免影响生产数据 