# SummerPlan Manager API 测试脚本

本目录包含了用于测试 SummerPlan Manager 后端 API 的 Shell 脚本，支持用户、计划、任务、进度等主要功能的自动化验证。

## 测试脚本列表

### 1. 用户与认证相关接口

- `test_login.sh` - 测试用户登录接口 (`POST /api/auth/login`)
- `test_register.sh` - 测试用户注册接口 (`POST /api/auth/register`)
- `test_me.sh` - 测试获取当前用户信息接口 (`GET /api/auth/me`)
- `test_profile.sh` - 测试获取用户详细信息接口 (`GET /api/auth/profile`)
- `test_logout.sh` - 测试用户登出接口 (`POST /api/auth/logout`)

### 2. 计划与任务相关接口

- `test_create_plan.sh` - 测试创建计划接口 (`POST /api/plans`)
- `test_get_plans.sh` - 测试获取所有计划接口 (`GET /api/plans`)
- `test_get_plan_detail.sh` - 测试获取计划详情接口 (`GET /api/plans/{planId}`)
- `test_update_plan.sh` - 测试更新计划接口 (`PUT /api/plans/{planId}`)
- `test_delete_plan.sh` - 测试删除计划接口 (`DELETE /api/plans/{planId}`)
- `test_create_task.sh` - 测试创建任务接口 (`POST /api/plans/{planId}/tasks`)
- `test_get_tasks.sh` - 测试获取计划下所有任务接口 (`GET /api/plans/{planId}/tasks`)
- `test_update_task.sh` - 测试更新任务接口 (`PUT /api/tasks/{taskId}`)
- `test_delete_task.sh` - 测试删除任务接口 (`DELETE /api/tasks/{taskId}`)

### 3. 任务进度相关接口

- `test_task_progress_post.sh` - 测试记录/修改任务进度 (`POST /api/tasks/{taskId}/progress`)
- `test_task_progress_get.sh` - 测试获取任务进度历史 (`GET /api/tasks/{taskId}/progress`)

### 4. 综合/串联测试脚本

- `test_all_apis.sh` - 综合测试所有主要API接口，包含完整流程和结果统计
- `test_all_progress_api.sh` - 串联测试计划、任务、进度等全流程
- `run_tests.sh` - 一键运行所有主要测试脚本
- `test_debug_me.sh` - 用于调试和自定义测试

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

# 运行计划相关测试
./test_create_plan.sh
./test_get_plans.sh

# 运行任务相关测试
./test_create_task.sh 1
./test_get_tasks.sh 1

# 运行进度相关测试（需先获取token和taskId）
./test_task_progress_post.sh <token> <taskId> <progress> [comment]
./test_task_progress_get.sh <token> <taskId>
```

### 运行综合/串联测试

```bash
# 运行所有API的综合测试
./test_all_apis.sh

# 串联测试计划-任务-进度全流程
./test_all_progress_api.sh
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

### 计划与任务相关接口（部分）

| 接口 | 方法 | 路径 | 描述 | 是否需要认证 |
|------|------|------|------|-------------|
| 创建计划 | POST | `/api/plans` | 新建计划 | 是 |
| 获取所有计划 | GET | `/api/plans` | 查询所有计划 | 是 |
| 获取计划详情 | GET | `/api/plans/{planId}` | 查询计划详情 | 是 |
| 更新计划 | PUT | `/api/plans/{planId}` | 修改计划 | 是 |
| 删除计划 | DELETE | `/api/plans/{planId}` | 删除计划 | 是 |
| 创建任务 | POST | `/api/plans/{planId}/tasks` | 新建任务 | 是 |
| 获取任务列表 | GET | `/api/plans/{planId}/tasks` | 查询计划下所有任务 | 是 |
| 更新任务 | PUT | `/api/tasks/{taskId}` | 修改任务 | 是 |
| 删除任务 | DELETE | `/api/tasks/{taskId}` | 删除任务 | 是 |

### 任务进度相关接口

| 接口 | 方法 | 路径 | 描述 | 是否需要认证 |
|------|------|------|------|-------------|
| 记录/修改进度 | POST | `/api/tasks/{taskId}/progress` | 新增或修改任务进度 | 是 |
| 获取进度历史 | GET | `/api/tasks/{taskId}/progress` | 查询任务进度历史 | 是 |

## 请求/响应示例

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

#### 创建计划请求
```json
{
  "title": "测试计划",
  "description": "测试计划描述",
  "startDate": "2024-07-01",
  "endDate": "2024-08-31"
}
```

#### 创建任务请求
```json
{
  "title": "测试任务",
  "description": "测试任务描述",
  "dueDate": "2024-07-10",
  "status": "TODO",
  "priority": "MEDIUM",
  "progress": 0
}
```

## 测试覆盖场景

### 正常流程测试
- ✅ 用户注册/登录/登出
- ✅ 计划创建/查询/更新/删除
- ✅ 任务创建/查询/更新/删除
- ✅ 任务进度记录/查询/修改

### 异常场景测试
- ✅ 重复注册（应失败）
- ✅ 错误密码登录（应失败）
- ✅ 无效token访问受保护接口（应失败）
- ✅ 非法参数、缺失参数等

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

如需添加新的API接口测试，可参考现有脚本格式：

1. 创建新的测试脚本文件
2. 添加适当的请求头和认证信息
3. 验证响应状态码和内容
4. 更新综合测试脚本

## 注意事项

- 所有测试脚本默认使用 `admin/admin123` 作为测试用户
- 测试脚本会创建临时用户和数据，可能影响数据库状态
- 建议在测试环境中运行，避免影响生产数据 