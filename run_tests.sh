#!/bin/bash

echo "🚀 开始运行 SummerPlan Manager API 测试套件"
echo "=========================================="
echo ""

# 检查后端服务是否运行
echo "检查后端服务状态..."
if curl -s http://localhost:8080/api/auth/login > /dev/null 2>&1; then
    echo "✅ 后端服务正在运行"
else
    echo "❌ 后端服务未运行，请先启动后端服务"
    echo "提示: 确保后端服务运行在 http://localhost:8080"
    exit 1
fi

echo ""

# 运行综合测试
echo "运行综合API测试..."
./test_all_apis.sh

echo ""
echo "=========================================="
echo "🎯 测试完成！"
echo ""
echo "如需运行单个测试，请使用以下命令："
echo "  ./test_login.sh     - 测试登录"
echo "  ./test_register.sh  - 测试注册"
echo "  ./test_me.sh        - 测试获取用户信息"
echo "  ./test_profile.sh   - 测试获取用户详细信息"
echo "  ./test_logout.sh    - 测试登出"
echo ""
echo "详细说明请查看 API_TESTS_README.md" 