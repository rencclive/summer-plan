# SummerPlan Manager - 暑期计划管理系统

一个基于Spring Boot 3.x + Vue 3的现代化暑期计划管理系统，帮助用户制定、跟踪和管理暑期计划。

## 技术栈

### 后端
- **Spring Boot 3.2.0** - 主框架
- **MySQL 8.0** - 数据库
- **Spring Data JPA** - 数据访问层
- **Spring Security** - 安全框架
- **JWT** - 身份认证
- **Flyway** - 数据库迁移
- **SpringDoc OpenAPI 3.0** - API文档
- **Maven** - 构建工具

### 前端
- **Vue 3** - 前端框架
- **TypeScript** - 类型安全
- **Element Plus** - UI组件库
- **Vue Router** - 路由管理
- **Pinia** - 状态管理
- **Vite** - 构建工具
- **Axios** - HTTP客户端

## 项目结构

```
summer-plan/
├── pom.xml                                    # 父POM文件
├── summerplan-manager-common/                 # 公共模块
│   ├── pom.xml
│   └── src/main/java/com/summerplan/common/
│       ├── constant/                          # 常量定义
│       ├── response/                          # 统一响应
│       ├── exception/                         # 异常处理
│       └── dto/                               # 数据传输对象
├── summerplan-manager-backend/                # 后端模块
│   ├── pom.xml
│   ├── src/main/java/com/summerplan/
│   │   ├── SummerPlanManagerApplication.java  # 启动类
│   │   ├── entity/                            # 实体类
│   │   ├── repository/                        # 数据访问层
│   │   ├── service/                           # 业务逻辑层
│   │   ├── controller/                        # 控制器层
│   │   ├── config/                            # 配置类
│   │   └── security/                          # 安全配置
│   └── src/main/resources/
│       ├── application.yml                    # 主配置文件
│       ├── application-dev.yml                # 开发环境配置
│       ├── application-prod.yml               # 生产环境配置
│       └── db/migration/                      # 数据库迁移脚本
└── summerplan-manager-frontend/               # 前端模块
    ├── package.json
    ├── vite.config.ts
    ├── tsconfig.json
    ├── index.html
    └── src/
        ├── main.ts                            # 入口文件
        ├── App.vue                            # 根组件
        ├── router/                            # 路由配置
        ├── stores/                            # 状态管理
        ├── api/                               # API接口
        ├── utils/                             # 工具函数
        ├── types/                             # 类型定义
        ├── views/                             # 页面组件
        ├── components/                        # 公共组件
        └── layouts/                           # 布局组件
```

## 快速开始

### 环境要求
- JDK 17+
- Node.js 18+
- MySQL 8.0+
- Maven 3.6+

### 后端启动

1. **创建数据库**
```sql
CREATE DATABASE summerplan_manager CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. **修改数据库配置**
编辑 `summerplan-manager-backend/src/main/resources/application.yml`，修改数据库连接信息：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/summerplan_manager?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: your_username
    password: your_password
```

3. **启动后端服务**
```bash
cd summerplan-manager-backend
mvn spring-boot:run
```

后端服务将在 `http://localhost:8080` 启动，API文档地址：`http://localhost:8080/api/swagger-ui.html`

### 前端启动

1. **安装依赖**
```bash
cd summerplan-manager-frontend
npm install
```

2. **启动开发服务器**
```bash
npm run dev
```

前端应用将在 `http://localhost:3000` 启动

### 默认账户
- 用户名：`admin`
- 密码：`admin123`

## 功能特性

### 用户管理
- 用户注册/登录
- 个人资料管理
- 密码修改
- 角色权限控制

### 计划管理
- 创建/编辑/删除暑期计划
- 计划详情查看
- 计划状态跟踪
- 计划标签分类

### 任务管理
- 计划任务创建/编辑/删除
- 任务进度跟踪与历史记录
- 任务优先级设置
- 工时统计

### 进度管理
- 任务进度记录与修改
- 进度历史查询

### 数据统计
- 计划完成率统计
- 任务进度分析
- 时间投入分析
- 可视化图表（部分功能开发中）

## API文档

启动后端服务后，访问以下地址查看API文档：
- Swagger UI: `http://localhost:8080/api/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api/api-docs`

## API测试脚本

本项目根目录下提供了丰富的Shell脚本用于API自动化测试，详见 [API_TESTS_README.md](./API_TESTS_README.md)。

- 单接口测试：如 `test_login.sh`、`test_register.sh`、`test_create_plan.sh`、`test_create_task.sh` 等
- 综合/串联测试：如 `test_all_apis.sh`、`test_all_progress_api.sh`
- 进度相关测试：如 `test_task_progress_post.sh`、`test_task_progress_get.sh`

请参考 `API_TESTS_README.md` 获取详细用法和接口说明。

## 开发指南

### 后端开发
1. 遵循RESTful API设计规范
2. 使用统一的响应格式
3. 实现完整的异常处理
4. 添加必要的日志记录
5. 编写单元测试

### 前端开发
1. 使用TypeScript确保类型安全
2. 遵循Vue 3 Composition API规范
3. 使用Element Plus组件库
4. 实现响应式设计，兼容移动端
5. 优化性能和用户体验

## 部署

### 后端部署
```bash
cd summerplan-manager-backend
mvn clean package
java -jar target/summerplan-manager-backend-1.0.0.jar
```

### 前端部署
```bash
cd summerplan-manager-frontend
npm run build
# 将dist目录部署到Web服务器
```

## Docker 启动方式

1. 在项目根目录执行以下命令构建所有镜像：
   ```bash
   docker-compose build
   ```

2. 构建完成后启动所有服务：
   ```bash
   docker-compose up -d
   ```

3. 如需关闭所有服务：
   ```bash
   docker-compose down
   ```

## 贡献指南

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/xxx`)
3. 提交更改 (`git commit -m 'Add some feature'`)
4. 推送到分支 (`git push origin feature/xxx`)
5. 打开 Pull Request

## 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 联系方式

- 项目维护者：rencclive
- 邮箱：rencc@live.com
- 项目地址：https://github.com/rencclive/summer-plan